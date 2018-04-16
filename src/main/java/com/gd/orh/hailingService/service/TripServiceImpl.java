package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Trip;
import com.gd.orh.hailingService.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Override
    public Trip create(Trip trip) {
        return tripRepository.save(trip);
    }
}
