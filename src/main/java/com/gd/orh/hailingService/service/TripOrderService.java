package com.gd.orh.hailingService.service;

import com.gd.orh.entity.TripOrder;

public interface TripOrderService {
    TripOrder acceptTripOrder(TripOrder tripOrder);

    boolean isTripOrderExisted(Long tripOrderId);

    TripOrder findById(Long tripOrderId);

    void confirmPickUp(TripOrder tripOrder);

    void confirmArrival(TripOrder tripOrder);
}
