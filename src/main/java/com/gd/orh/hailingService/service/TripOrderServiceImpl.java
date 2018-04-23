package com.gd.orh.hailingService.service;

import com.gd.orh.entity.*;
import com.gd.orh.mapper.FareMapper;
import com.gd.orh.mapper.TripMapper;
import com.gd.orh.mapper.TripOrderMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class TripOrderServiceImpl implements TripOrderService {

    @Autowired
    private TripOrderMapper tripOrderMapper;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private FareMapper fareMapper;

    @Override
    public TripOrder acceptTripOrder(TripOrder tripOrder) {
        Fare fare = tripOrder.getFare();
        fareMapper.insertFare(fare);

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
    public TripOrder confirmPickUp(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.PROCESSING);
        tripOrderMapper.updateOrderStatus(tripOrder);

        tripOrder.getTrip().setTripStatus(TripStatus.WAS_PICK_UP);
        tripMapper.updateTripStatus(tripOrder.getTrip());

        return tripOrder;
    }

    @Override
    public TripOrder confirmArrival(TripOrder tripOrder) {
        tripOrder.setCompletedTime(new Date());
        tripOrderMapper.updateCompletedTime(tripOrder);

        tripOrder.setOrderStatus(OrderStatus.COMPLETED);
        tripOrderMapper.updateOrderStatus(tripOrder);

        fareMapper.updateFare(tripOrder.getFare());

        tripOrder.getTrip().setTripStatus(TripStatus.ARRIVED);
        tripMapper.updateTripStatus(tripOrder.getTrip());

        return tripOrder;
    }

    @Override
    public List<TripOrder> findAllByPassenger(Passenger passenger) {
        if (passenger.getPage() != null && passenger.getRows() != null)
            PageHelper.startPage(passenger.getPage(), passenger.getRows());

        return tripOrderMapper.findAllByPassengerId(passenger.getId());
    }
}
