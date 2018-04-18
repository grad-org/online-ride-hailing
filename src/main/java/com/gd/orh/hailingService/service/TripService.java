package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Trip;

import java.util.List;

public interface TripService {

    Trip publishTrip(Trip trip);

    List<Trip> findPublishedTrip(Trip trip);

    List<Trip> findByTripStatus(Trip trip);

    Trip findById(Long id);
}
