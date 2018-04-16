package com.gd.orh.hailingService.controller;

import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.hailingService.service.PassengerService;
import com.gd.orh.hailingService.service.TripService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/hailingService")
public class HailingServiceController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private TripService tripService;

    // 发布行程
    @PostMapping("/passenger/{id}/trip")
    public ResponseEntity<?> publishTrip(@PathVariable Long id, @RequestBody Trip publishedTrip) {
        Passenger passenger = passengerService.findById(id);

        publishedTrip.setCreatedTime(new Date());
        publishedTrip.setTripStatus(TripStatus.PUBLISHED);
        publishedTrip.setPassenger(passenger);

        Trip trip = tripService.create(publishedTrip);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(trip));
    }
}
