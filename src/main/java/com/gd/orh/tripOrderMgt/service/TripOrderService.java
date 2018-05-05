package com.gd.orh.tripOrderMgt.service;

import com.gd.orh.entity.TripOrder;

import java.util.List;

public interface TripOrderService {
    TripOrder acceptTripOrder(TripOrder tripOrder);

    boolean isTripOrderExisted(Long id);

    TripOrder findById(Long id);

    TripOrder confirmPickUp(TripOrder tripOrder);

    TripOrder confirmArrival(TripOrder tripOrder);

    List<TripOrder> findAllByPassenger(TripOrder tripOrder);

    List<TripOrder> findAllByDriver(TripOrder tripOrder);

    boolean isTripOrderCanBePaid(TripOrder tripOrder);

    TripOrder payTripOrder(TripOrder tripOrder);

    TripOrder completePayment(TripOrder tripOrder);

    TripOrder cancelOrder(TripOrder tripOrder);
}
