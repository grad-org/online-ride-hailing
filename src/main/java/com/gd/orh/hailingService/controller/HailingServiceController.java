package com.gd.orh.hailingService.controller;

import com.gd.orh.dto.CarLocationDTO;
import com.gd.orh.dto.TripDTO;
import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.entity.*;
import com.gd.orh.hailingService.service.DriverService;
import com.gd.orh.hailingService.service.PassengerService;
import com.gd.orh.hailingService.service.TripOrderService;
import com.gd.orh.hailingService.service.TripService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hailingService")
public class HailingServiceController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TripService tripService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private TripOrderService tripOrderService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // 发布行程,广播给正在听单的车主
    @MessageMapping("/hailingService/trip/publishTrip")
    @SendTo("/topic/hailingService/trip/publishTrip")
    public Trip publishTrip(TripDTO tripDTO) {
        Trip trip = tripDTO.convertToTrip();

        Passenger passenger = passengerService.findById(trip.getPassenger().getId());
        trip.setPassenger(passenger);

        Trip publishedTrip = tripService.publishTrip(trip);

        return publishedTrip;
    }

    // 车主上传车辆位置,广播给乘客
    @MessageMapping("/hailingService/car/uploadCarLocation")
    @SendTo("/topic/hailingService/car/uploadCarLocation")
    public CarLocationDTO broadcastLocation(CarLocationDTO carLocationDTO) {

        return carLocationDTO;
    }

    // 车主上传车辆位置，单独发给乘客
    @MessageMapping("/queue/hailingService/car/uploadCarLocation/{passengerUsername}")
    public void uploadLocation(
            @DestinationVariable("passengerUsername") String passengerUsername,
            @Payload CarLocationDTO carLocationDTO) {
        // 通知乘客接单
        simpMessagingTemplate
            .convertAndSendToUser(
                passengerUsername,
                "/queue/hailingService/car/uploadCarLocation",
                carLocationDTO);
    }

    // 车主受理订单
    @PostMapping("/tripOrder/acceptTripOrder")
    public ResponseEntity<?> acceptTripOrder(@RequestBody TripOrderDTO tripOrderDTO) {

        Long tripId = tripOrderDTO.getTripId();

        // 行程不存在
        if (!tripService.isTripExisted(tripId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                        RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The trip is not existed!",
                            null
                    ));
        }

        Trip trip = tripService.findById(tripId);

        // 当前行程状态不允许被受理
        if (trip.getTripStatus().ordinal() > TripStatus.PUBLISHED.ordinal()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip could not be accepted!"));
        }

        Long driverId = tripOrderDTO.getDriverId();

        Driver driver = driverService.findById(driverId);

        TripOrder tripOrder = new TripOrder();
        tripOrder.setDriver(driver);
        tripOrder.setTrip(trip);

        // 车主受理订单
        tripOrderService.acceptTripOrder(tripOrder);

        // 通知乘客接单
        simpMessagingTemplate
            .convertAndSendToUser(
                trip.getPassenger().getUser().getUsername(),
                "/queue/hailingService/tripOrder/acceptance-notification",
                tripOrder
            );

        return ResponseEntity
                .ok()
                .body(RestResultFactory.getSuccessResult(tripOrder));
    }

    // 确认乘客上车
    @PostMapping("/tripOrder/pickUpPassenger")
    public ResponseEntity<?> pickupPassenger(@RequestBody TripOrderDTO tripOrderDTO) {
        Long tripOrderId = tripOrderDTO.getTripOrderId();

        // 行程订单不存在
        if (!tripOrderService.isTripOrderExisted(tripOrderId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "The trip order is not existed!",
                        null
                    ));
        }

        TripOrder tripOrder = tripOrderService.findById(tripOrderId);
        Long tripId = tripOrderDTO.getTripId();

        // 订单对应的行程不一致
        if (!tripOrder.getTrip().getId().equals(tripId)) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "The giving trip is not available!",
                            null
                    ));
        }

        // 当前行程状态不允许车主进行确认上车
        if (tripOrder.getOrderStatus().ordinal() > OrderStatus.ACCEPTED.ordinal()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be processed!"));
        }

        Trip trip = tripService.findById(tripId);
        tripOrder.setTrip(trip);

        Long driverId = tripOrderDTO.getDriverId();
        Driver driver = driverService.findById(driverId);
        tripOrder.setDriver(driver);

        // 确认乘客上车
        tripOrderService.confirmPickUp(tripOrder);

        return ResponseEntity
                .ok()
                .body(RestResultFactory.getSuccessResult(tripOrder));
    }
}
