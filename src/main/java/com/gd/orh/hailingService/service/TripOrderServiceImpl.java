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

    @Override
    @Transactional(readOnly = true)
    public boolean isTripOrderExisted(Long tripOrderId) {
        return tripOrderMapper.existsWithPrimaryKey(tripOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public TripOrder findById(Long tripOrderId) {
        return tripOrderMapper.findById(tripOrderId);
    }

    @Override
    public void confirmPickUp(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.PROCESSING);
        tripOrder.getTrip().setTripStatus(TripStatus.WAS_PICK_UP);
        tripOrderMapper.updateOrderStatus(tripOrder);
        tripMapper.updateTripStatus(tripOrder.getTrip());
    }
}
