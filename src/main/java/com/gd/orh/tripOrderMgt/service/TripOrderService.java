package com.gd.orh.tripOrderMgt.service;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.TripOrder;

import java.util.List;

public interface TripOrderService {
    TripOrder acceptTripOrder(TripOrder tripOrder);

    boolean isTripOrderExisted(Long id);

    TripOrder findById(Long id);

    TripOrder confirmPickUp(TripOrder tripOrder);

    TripOrder confirmArrival(TripOrder tripOrder);

    List<TripOrder> findAllByPassenger(Passenger passenger);

    List<TripOrder> findAllByDriver(Driver driver);
}
