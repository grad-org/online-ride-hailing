package com.gd.orh.mapper;

import com.gd.orh.entity.Trip;
import com.gd.orh.entity.ListeningOrderCondition;

import java.util.List;

public interface TripMapper {
    void insertTrip(Trip trip);

    Trip findById(Long id);

    List<Trip> findByTripStatus(Trip trip);

    void updateTripStatus(Trip trip);

    List<Trip> searchPublishedTripsByCondition(ListeningOrderCondition tripCondition);
}