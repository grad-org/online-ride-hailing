package com.gd.orh.tripOrderMgt.service;

import com.gd.orh.entity.ListeningOrderCondition;
import com.gd.orh.entity.Trip;

import java.util.List;

public interface TripService {

    Trip publishTrip(Trip trip);

    List<Trip> findPublishedTripsByListeningOrderCondition(ListeningOrderCondition condition);

    List<Trip> findByTripStatus(Trip trip);

    Trip findById(Long id);

    boolean isTripExisted(Long id);
}
