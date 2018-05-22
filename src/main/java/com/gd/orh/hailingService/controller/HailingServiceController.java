package com.gd.orh.hailingService.controller;

import com.gd.orh.dto.TripDTO;
import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.entity.*;
import com.gd.orh.tripOrderMgt.service.TripOrderService;
import com.gd.orh.tripOrderMgt.service.TripService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hailingService")
public class HailingServiceController {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripOrderService tripOrderService;

    // 发布行程,广播给正在听单的车主
    @PostMapping("/trip/publishTrip")
    public ResponseEntity<?> publishTrip(@RequestBody TripDTO tripDTO) {
        Trip trip = tripDTO.convertTo();

        Trip publishedTrip = tripService.publishTrip(trip);

        TripDTO publishedTripDTO = new TripDTO().convertFor(publishedTrip);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(publishedTripDTO));
    }

    // 取消行程
    @PostMapping("/trip/cancelTrip")
    public ResponseEntity<?> cancelTrip(@RequestBody TripDTO tripDTO) {
        Trip trip = tripDTO.convertTo();

        // 行程不存在
        if (!tripService.isTripExisted(trip.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The trip is not existed!",
                            null
                    ));
        }

        trip = tripService.findById(trip.getId());

        // 当前行程状态不允许被取消
        if (trip.getTripStatus() != TripStatus.PUBLISHED) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip could not be canceled!"));
        }

        Trip canceledTrip = tripService.cancelTrip(trip);

        TripDTO canceledTripDTO = new TripDTO().convertFor(canceledTrip);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(canceledTripDTO));
    }

    // 受理订单
    @PostMapping("/tripOrder/acceptTripOrder")
    public ResponseEntity<?> acceptTripOrder(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertTo();

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

        // 车主受理订单
        TripOrder acceptedTripOrder = tripOrderService.acceptTripOrder(tripOrder);

        TripOrderDTO acceptedTripOrderDTO = new TripOrderDTO().convertFor(acceptedTripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(acceptedTripOrderDTO));
    }

    // 乘客取消订单
    @PostMapping("/tripOrder/cancelTripOrderByPassenger")
    public ResponseEntity<?> cancelTripOrderByPassenger(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertTo();

        // 行程订单不存在
        if (!tripOrderService.isTripOrderExisted(tripOrder.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The trip order is not existed!",
                            null
                    ));
        }

        tripOrder = tripOrderService.findById(tripOrder.getId());

        // 当前行程状态不允许车主取消订单
        if (tripOrder.getOrderStatus() != OrderStatus.ACCEPTED) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be cancel from passenger!"));
        }

        // 取消行程订单
        TripOrder canceledTripOrder = tripOrderService.cancelOrder(tripOrder);

        TripOrderDTO canceledTripOrderDTO = new TripOrderDTO().convertFor(canceledTripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(canceledTripOrderDTO));
    }

    // 车主取消订单
    @PostMapping("/tripOrder/cancelTripOrderByDriver")
    public ResponseEntity<?> cancelTripOrderByDriver(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertTo();

        // 行程订单不存在
        if (!tripOrderService.isTripOrderExisted(tripOrder.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The trip order is not existed!",
                            null
                    ));
        }

        tripOrder = tripOrderService.findById(tripOrder.getId());

        // 当前行程状态不允许乘客取消订单
        if (tripOrder.getOrderStatus() != OrderStatus.ACCEPTED) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be cancel from driver!"));
        }

        // 取消行程订单
        TripOrder canceledTripOrder = tripOrderService.cancelOrder(tripOrder);

        TripOrderDTO canceledTripOrderDTO = new TripOrderDTO().convertFor(canceledTripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(canceledTripOrderDTO));
    }

    // 确认乘客上车
    @PostMapping("/tripOrder/pickUpPassenger")
    public ResponseEntity<?> pickUpPassenger(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertTo();

        // 行程订单不存在
        if (!tripOrderService.isTripOrderExisted(tripOrder.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "The trip order is not existed!",
                        null
                    ));
        }

        tripOrder = tripOrderService.findById(tripOrder.getId());

        // 当前行程状态不允许车主进行确认上车
        if (tripOrder.getOrderStatus() != OrderStatus.ACCEPTED) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be processed!"));
        }

        // 确认乘客上车
        TripOrder pickedUpTripOrder = tripOrderService.confirmPickUp(tripOrder);

        TripOrderDTO pickedUpTripOrderDTO = new TripOrderDTO().convertFor(pickedUpTripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(pickedUpTripOrderDTO));
    }

    // 确认到达
    @PostMapping("/tripOrder/confirmArrival")
    public ResponseEntity<?> confirmArrival(@RequestBody TripOrderDTO tripOrderDTO) {
        TripOrder tripOrder = tripOrderDTO.convertTo();

        // 行程订单不存在
        if (!tripOrderService.isTripOrderExisted(tripOrder.getId())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The trip order is not existed!",
                            null
                    ));
        }

        tripOrder = tripOrderService.findById(tripOrder.getId());

        // 当前行程状态不允许车主进行确认到达
        if (tripOrder.getOrderStatus() != OrderStatus.PROCESSING) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The trip order could not be confirmed!"));
        }
        
        tripOrder.getFare().setLengthOfMileage(tripOrderDTO.getLengthOfMileage());
        tripOrder.getFare().setLengthOfTime(tripOrderDTO.getLengthOfTime());

        // 确认到达
        TripOrder arrivalTripOrder = tripOrderService.confirmArrival(tripOrder);

        TripOrderDTO arrivalTripOrderDTO = new TripOrderDTO().convertFor(arrivalTripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(arrivalTripOrderDTO));
    }
}
