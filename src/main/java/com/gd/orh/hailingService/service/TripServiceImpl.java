package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.mapper.TripMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripMapper tripMapper;

    @Override
    public Trip publishTrip(Trip trip) {
        trip.setCreatedTime(new Date());
        trip.setTripStatus(TripStatus.PUBLISHED);

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
    @Transactional(readOnly = true)
    public Trip findById(Long id) {
        return tripMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTripExisted(Long id) {
        return tripMapper.existsWithPrimaryKey(id);
    }
}
