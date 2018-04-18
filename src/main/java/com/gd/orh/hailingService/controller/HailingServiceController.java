package com.gd.orh.hailingService.controller;

import com.gd.orh.entity.*;
import com.gd.orh.external.UserManageFacade;
import com.gd.orh.hailingService.service.DriverService;
import com.gd.orh.hailingService.service.PassengerService;
import com.gd.orh.hailingService.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

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
    private UserManageFacade userManageFacade;

    // 发布行程,广播给正在听单的车主
    @MessageMapping("/passenger/{passengerId}/publishTrip")
    @SendTo("/topic/passenger/publishTrip")
    public Trip publishTrip(@DestinationVariable("passengerId") Long passengerId, Trip publishedTrip) {
        Passenger passenger = passengerService.findById(passengerId);
        publishedTrip.setPassenger(passenger);

        publishedTrip.setCreatedTime(new Date());
        publishedTrip.setTripStatus(TripStatus.PUBLISHED);

        Trip trip = tripService.publishTrip(publishedTrip);

        return trip;
    }

    // 车主上传车辆位置,广播给乘客
    @MessageMapping("/driver/uploadCarLocation")
    @SendTo("/topic/driver/uploadCarLocation")
    public CarLocation uploadLocation(Location location, Principal principal) {
        CarLocation carLocation = new CarLocation();

        User user = userManageFacade.findUserByPrincipal(principal);

        carLocation.setCarId(user.getNickname());
        carLocation.setLocation(location);

        return carLocation;
    }

    @PostMapping("/driver/{driverId}/acceptTripOrder")
    public ResponseEntity<?> acceptTripOrder(
            @PathVariable("driverId") Long driverId,
            @RequestParam("tripId") Long tripId) {
        Trip trip = tripService.findById(tripId);
        Driver driver = driverService.findById(driverId);

        TripOrder tripOrder = new TripOrder();
        tripOrder.setDriver(driver);
        tripOrder.setTrip(trip);
        tripOrder.setAcceptedTime(new Date());
        tripOrder.setOrderStatus(OrderStatus.ACCEPTED);


    }
}
