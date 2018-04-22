package com.gd.orh.mapper;

import com.gd.orh.entity.Fare;
import com.gd.orh.utils.MyMapper;

public interface FareMapper extends MyMapper<Fare> {
    void insertFare(Fare fare);
}