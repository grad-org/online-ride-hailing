package com.gd.orh.mapper;

import com.gd.orh.entity.DriverRating;
import com.gd.orh.utils.MyMapper;

public interface DriverRatingMapper extends MyMapper<DriverRating> {
    void insertDriverRating(DriverRating driverRating);

    DriverRating findById(Long id);
}