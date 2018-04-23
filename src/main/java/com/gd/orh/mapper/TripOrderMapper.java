package com.gd.orh.mapper;

import com.gd.orh.entity.TripOrder;
import com.gd.orh.utils.MyMapper;

public interface TripOrderMapper extends MyMapper<TripOrder> {
    void insertTripOrder(TripOrder tripOrder);

    TripOrder findById(Long id);

    void updateOrderStatus(TripOrder tripOrder);

    void updateCompletedTime(TripOrder tripOrder);
}