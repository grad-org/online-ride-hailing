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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MessageMapping("/hailingService")
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
    @MessageMapping("/trip/publishTrip")
    @SendTo("/topic/hailingService/trip/publishTrip")
    public Trip publishTrip(TripDTO tripDTO) {
        Trip trip = tripDTO.convertToTrip();

        Passenger passenger = passengerService.findById(trip.getPassenger().getId());
        trip.setPassenger(passenger);

        Trip publishedTrip = tripService.publishTrip(trip);

        return publishedTrip;
    }

    // 车主上传车辆位置,广播给乘客
    @MessageMapping("/car/uploadCarLocation")
    @SendTo("/topic/hailingService/car/uploadCarLocation")
    public CarLocationDTO uploadLocation(CarLocationDTO carLocationDTO) {

        return carLocationDTO;
    }

    // 车主受理订单
    @PostMapping("/tripOrder/acceptTripOrder")
    public ResponseEntity<?> acceptTripOrder(@RequestBody TripOrderDTO tripOrderDTO) {

        Long tripId = tripOrderDTO.getTripId();

        // 行程不存在
        if (!tripService.isTripExisted(tripId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND, "The trip is not existed!", null));
        }

        Trip trip = tripService.findById(tripId);

        // 当前行程状态不允许被受理
        if (trip.getTripStatus().ordinal() > TripStatus.PUBLISHED.ordinal()) {
            return ResponseEntity.badRequest().body(
                    RestResultFactory.getFailResult("The trip could not be accepted!"));
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
                "queue/hailingService/tripOrder/acceptance-notification",
                tripOrder
            );

        return ResponseEntity.ok().body(
                RestResultFactory.getSuccessResult(tripOrder));
    }
}
