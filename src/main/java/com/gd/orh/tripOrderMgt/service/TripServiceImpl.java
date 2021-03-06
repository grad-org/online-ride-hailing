package com.gd.orh.tripOrderMgt.service;

import com.gd.orh.entity.ListeningOrderCondition;
import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.entity.TripType;
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
        if(trip.getTripType() == TripType.REAL_TIME) {
            trip.setDepartureTime(new Date());
        }

        trip.setCreatedTime(new Date());
        trip.setTripStatus(TripStatus.PUBLISHED);

        tripMapper.insertTrip(trip);

        return this.findById(trip.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trip> findPublishedTripsByListeningOrderCondition(ListeningOrderCondition condition) {
        condition.setDepartureTime(new Date());

        if (condition.getPage() != null && condition.getRows() != null) {
            PageHelper.startPage(condition.getPage(), condition.getRows());
        }
        return tripMapper.searchPublishedTripsByCondition(condition);
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
        return this.findById(id) != null;
    }

    @Override
    public Trip cancelTrip(Trip trip) {
        trip.setTripStatus(TripStatus.CANCELED);

        tripMapper.updateTripStatus(trip);

        return this.findById(trip.getId());
    }
}
