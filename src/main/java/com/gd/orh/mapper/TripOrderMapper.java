package com.gd.orh.mapper;

import com.gd.orh.entity.TripOrder;
import com.gd.orh.utils.MyMapper;

public interface TripOrderMapper extends MyMapper<TripOrder> {
    Long insertTripOrder(TripOrder tripOrder);

    TripOrder findById(Long id);
}