package com.gd.orh.mapper;

import com.gd.orh.entity.Trip;
import com.gd.orh.utils.MyMapper;

public interface TripMapper extends MyMapper<Trip> {
    Long insertTrip(Trip trip);

    Trip findById(Long id);
}