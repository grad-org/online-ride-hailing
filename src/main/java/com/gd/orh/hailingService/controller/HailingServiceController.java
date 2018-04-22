package com.gd.orh.hailingService.controller;

import com.gd.orh.dto.CarLocationDTO;
import com.gd.orh.dto.FareDTO;
import com.gd.orh.dto.TripDTO;
import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.entity.*;
import com.gd.orh.hailingService.service.*;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    private FareRuleService fareRuleService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // 发布行程,广播给正在听单的车主
    @PostMapping("/trip/publishTrip")
    public ResponseEntity<?> publishTrip(@RequestBody TripDTO tripDTO) {
        Trip trip = tripDTO.convertToTrip();

        Passenger passenger = passengerService.findById(trip.getPassenger().getId());

        trip.setPassenger(passenger);

        trip = tripService.publishTrip(trip);

        TripDTO publishedTripDTO = new TripDTO().convertFor(trip);

        publishedTripDTO.setDepartureLocation(tripDTO.getDepartureLocation());

        simpMessagingTemplate.convertAndSend(
            "/topic/hailingService/trip/publishTrip",
            publishedTripDTO
        );

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(publishedTripDTO));
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

        simpMessagingTemplate.convertAndSendToUser(
            passengerUsername,
            "/queue/hailingService/car/uploadCarLocation",
            carLocationDTO
        );
    }

    // 受理订单
    @PostMapping("/tripOrder/acceptTripOrder")
    public ResponseEntity<?> acceptTripOrder(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertToTripOrder();

        // 行程不存在
        if (!tripService.isTripExisted(tripOrder.getTrip().getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "The trip is not existed!",
                        null
                    ));
        }

        Trip trip = tripService.findById(tripOrder.getTrip().getId());

        // 当前行程状态不允许被受理
        if (trip.getTripStatus() != TripStatus.PUBLISHED) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip could not be accepted!"));
        }

        Driver driver = driverService.findById(tripOrder.getDriver().getId());

        tripOrder.setDriver(driver);
        tripOrder.setTrip(trip);

        // 车主受理订单
        tripOrder = tripOrderService.acceptTripOrder(tripOrder);

        TripOrderDTO acceptedTripOrderDTO = new TripOrderDTO().convertFor(tripOrder);

        // 通知乘客接单
        simpMessagingTemplate.convertAndSendToUser(
            trip.getPassenger().getUser().getUsername(),
            "/queue/hailingService/tripOrder/acceptance-notification",
            acceptedTripOrderDTO
        );

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(acceptedTripOrderDTO));
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
        if (tripOrder.getOrderStatus() != OrderStatus.ACCEPTED) {
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

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrder));
    }

    // 预估车费
    @GetMapping("/fare/predictFare")
    public ResponseEntity<?> predictFare(@Valid FareDTO fareDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                        "The giving mileage or time could not be empty and must be greater than 0!"
                    ));
        }

        Fare fare = fareDTO.convertToFare();

        // 获取最新计费规则
        FareRule recentFareRule = fareRuleService.findRecentFareRule();
        fare.setFareRule(recentFareRule);

        FareDTO predictFareDTO = new FareDTO().convertFor(fare);

        // 返回预估车费
        return ResponseEntity.ok(RestResultFactory.getSuccessResult(predictFareDTO));
    }

    // 确认到达
    @PostMapping("/tripOrder/confirmArrival")
    public ResponseEntity<?> confirmArrival(@RequestBody TripOrderDTO tripOrderDTO) {
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

        // 当前行程状态不允许车主进行确认到达
        if (tripOrder.getOrderStatus() != OrderStatus.PROCESSING) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be confirmed!"));
        }

        FareRule fareRule = fareRuleService.findById(tripOrderDTO.getFareRuleId());
        Fare fare = new Fare(
                tripOrderDTO.getLengthOfMileage(),
                tripOrderDTO.getLengthOfTime(),
                fareRule);
        tripOrder.setFare(fare);

        Trip trip = tripService.findById(tripId);
        tripOrder.setTrip(trip);

        Long driverId = tripOrderDTO.getDriverId();
        Driver driver = driverService.findById(driverId);
        tripOrder.setDriver(driver);

        // 确认到达
        tripOrderService.confirmArrival(tripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrder));
    }
}
