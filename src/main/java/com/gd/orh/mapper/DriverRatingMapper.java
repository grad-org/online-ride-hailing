package com.gd.orh.mapper;

import com.gd.orh.entity.DriverRating;

public interface DriverRatingMapper {
    void insertDriverRating(DriverRating driverRating);

    DriverRating findById(Long id);
}