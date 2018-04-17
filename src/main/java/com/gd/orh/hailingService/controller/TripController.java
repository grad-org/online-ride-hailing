package com.gd.orh.hailingService.controller;

import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.hailingService.service.PassengerService;
import com.gd.orh.hailingService.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class TripController {
    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TripService tripService;

    // 发布行程
    @MessageMapping("/trip")
    @SendTo("/topic/trip")
    public Trip publishTrip(Trip publishedTrip) {
        Passenger passenger = passengerService.findById(publishedTrip.getPassenger().getId());
        publishedTrip.setPassenger(passenger);

        publishedTrip.setCreatedTime(new Date());
        publishedTrip.setTripStatus(TripStatus.PUBLISHED);

        Trip trip = tripService.create(publishedTrip);

        return trip;
    }
}
