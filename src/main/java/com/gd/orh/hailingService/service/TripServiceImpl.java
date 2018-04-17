package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Trip;
import com.gd.orh.mapper.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripMapper tripMapper;

    @Override
    public Trip create(Trip trip) {
        tripMapper.insertTrip(trip);
        return trip;
    }
}
