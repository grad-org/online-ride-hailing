package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Trip;
import com.gd.orh.mapper.DriverMapper;
import com.gd.orh.mapper.PassengerMapper;
import com.gd.orh.mapper.TripMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private DriverMapper driverMapper;

    @Override
    public Trip publishTrip(Trip trip) {
        tripMapper.insertTrip(trip);
        return trip;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findPublishedTrip(Trip trip) {
        return this.findByTripStatus(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findByTripStatus(Trip trip) {
        if (trip.getPage() != null && trip.getRows() != null) {
            PageHelper.startPage(trip.getPage(), trip.getRows());
        }
        return tripMapper.findByTripStatus(trip);
    }

    @Override
    public Trip findById(Long id) {
        Trip trip = tripMapper.findById(id);
        trip.setPassenger(passengerMapper.findById(trip.getPassenger().getId()));
        trip.setDriver(driverMapper.findById(trip.getPassenger().getId()));

        return trip;
    }
}
