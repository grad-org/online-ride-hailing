package com.gd.orh.hailingService.controller;

import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.hailingService.service.TripService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripRestController {

    @Autowired
    private TripService tripService;

    @GetMapping("/published")
    public ResponseEntity<?> findPublishedTrip(Trip trip) {
        trip.setTripStatus(TripStatus.PUBLISHED);
        trip.setDepartureTime(new Date());

        List<Trip> trips = tripService.findPublishedTrip(trip);
        return ResponseEntity.ok(
                RestResultFactory.getSuccessResult().setData(trips));
    }
}
