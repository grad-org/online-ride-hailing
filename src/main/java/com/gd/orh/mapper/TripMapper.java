package com.gd.orh.mapper;

import com.gd.orh.entity.Trip;
import com.gd.orh.entity.ListeningOrderCondition;
import com.gd.orh.utils.MyMapper;

import java.util.List;

public interface TripMapper extends MyMapper<Trip> {
    void insertTrip(Trip trip);

    Trip findById(Long id);

    List<Trip> findByTripStatus(Trip trip);

    void updateTripStatus(Trip trip);

    List<Trip> searchPublishedTripsByCondition(ListeningOrderCondition tripCondition);
}