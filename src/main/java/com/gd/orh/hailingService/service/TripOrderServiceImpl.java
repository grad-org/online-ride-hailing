package com.gd.orh.hailingService.service;

import com.gd.orh.entity.OrderStatus;
import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripOrder;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.mapper.TripMapper;
import com.gd.orh.mapper.TripOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class TripOrderServiceImpl implements TripOrderService {

    @Autowired
    private TripOrderMapper tripOrderMapper;

    @Autowired
    private TripMapper tripMapper;

    @Override
    public TripOrder acceptTripOrder(TripOrder tripOrder) {
        tripOrder.setAcceptedTime(new Date());
        tripOrder.setOrderStatus(OrderStatus.ACCEPTED);
        tripOrderMapper.insertTripOrder(tripOrder);

        Trip trip = tripOrder.getTrip();
        trip.setTripStatus(TripStatus.ACCEPTED);

        tripMapper.updateTripStatus(trip);
        return tripOrder;
    }
}
