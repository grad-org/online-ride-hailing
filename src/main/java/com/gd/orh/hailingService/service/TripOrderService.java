package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.TripOrder;

import java.util.List;

public interface TripOrderService {
    TripOrder acceptTripOrder(TripOrder tripOrder);

    boolean isTripOrderExisted(Long tripOrderId);

    TripOrder findById(Long tripOrderId);

    TripOrder confirmPickUp(TripOrder tripOrder);

    TripOrder confirmArrival(TripOrder tripOrder);

    List<TripOrder> findAllByPassenger(Passenger passenger);
}
