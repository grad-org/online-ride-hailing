package com.gd.orh.tripOrderMgt.service;

import com.gd.orh.entity.*;
import com.gd.orh.mapper.FareMapper;
import com.gd.orh.mapper.FareRuleMapper;
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

    @Autowired
    private FareRuleMapper fareRuleMapper;

    @Override
    public TripOrder acceptTripOrder(TripOrder tripOrder) {
        FareRule fareRule = fareRuleMapper.findRecentFareRule();
        Fare fare = new Fare();
        fare.setFareRule(fareRule);
        fareMapper.insertFare(fare);

        tripOrder.setFare(fare);
        tripOrder.setAcceptedTime(new Date());
        tripOrder.setOrderStatus(OrderStatus.ACCEPTED);
        tripOrderMapper.insertTripOrder(tripOrder);

        Trip trip = tripOrder.getTrip();
        trip.setTripStatus(TripStatus.ACCEPTED);
        tripMapper.updateTripStatus(trip);

        return this.findById(tripOrder.getId());
    }

    @Override
    public TripOrder cancelOrder(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.CLOSED);
        tripOrderMapper.updateOrderStatus(tripOrder);

        Trip trip = tripOrder.getTrip();
        trip.setTripStatus(TripStatus.CANCELED);
        tripMapper.updateTripStatus(trip);

        return this.findById(tripOrder.getId());
    }

    @Override
    public TripOrder confirmPickUp(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.PROCESSING);
        tripOrderMapper.updateOrderStatus(tripOrder);

        tripOrder.getTrip().setTripStatus(TripStatus.WAS_PICK_UP);
        tripMapper.updateTripStatus(tripOrder.getTrip());

        return this.findById(tripOrder.getId());
    }

    @Override
    public TripOrder confirmArrival(TripOrder tripOrder) {
        tripOrder.setCompletedTime(new Date());
        tripOrderMapper.updateCompletedTime(tripOrder);

        tripOrder.setOrderStatus(OrderStatus.PROCESSING_COMPLETED);
        tripOrderMapper.updateOrderStatus(tripOrder);

        fareMapper.updateFare(tripOrder.getFare());

        tripOrder.getTrip().setTripStatus(TripStatus.ARRIVED);
        tripMapper.updateTripStatus(tripOrder.getTrip());

        return this.findById(tripOrder.getId());
    }

    @Override
    public TripOrder payTripOrder(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.PAID);
        tripOrderMapper.updateOrderStatus(tripOrder);

        return this.findById(tripOrder.getId());
    }

    @Override
    public TripOrder completePayment(TripOrder tripOrder) {
        tripOrder.setOrderStatus(OrderStatus.PAYMENT_COMPLETED);
        tripOrderMapper.updateOrderStatus(tripOrder);

        return this.findById(tripOrder.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTripOrderExisted(Long id) {
        return this.findById(id) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public TripOrder findById(Long id) {
        TripOrder tripOrder = tripOrderMapper.findById(id);

        Trip trip = tripMapper.findById(tripOrder.getTrip().getId());
        tripOrder.setTrip(trip);

        return tripOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripOrder> findAllByPassenger(TripOrder tripOrder) {
        if (tripOrder.getPage() != null && tripOrder.getRows() != null)
            PageHelper.startPage(tripOrder.getPage(), tripOrder.getRows());

        List<TripOrder> tripOrders = tripOrderMapper.findAllByPassenger(tripOrder);

        tripOrders.forEach(each -> {
            Trip trip = tripMapper.findById(each.getTrip().getId());
            each.setTrip(trip);
        });

        return tripOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripOrder> findAllByDriver(TripOrder tripOrder) {
        if (tripOrder.getPage() != null && tripOrder.getRows() != null)
            PageHelper.startPage(tripOrder.getPage(), tripOrder.getRows());

        List<TripOrder> tripOrders = tripOrderMapper.findAllByDriver(tripOrder);

        tripOrders.forEach(each -> {
            Trip trip = tripMapper.findById(each.getTrip().getId());
            each.setTrip(trip);
        });

        return tripOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTripOrderCanBePaid(TripOrder tripOrder) {
        return this.findById(tripOrder.getId()).getOrderStatus() == OrderStatus.PROCESSING_COMPLETED;
    }
}
