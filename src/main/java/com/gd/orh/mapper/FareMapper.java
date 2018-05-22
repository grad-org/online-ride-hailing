package com.gd.orh.mapper;

import com.gd.orh.entity.Fare;

public interface FareMapper {
    void insertFare(Fare fare);

    void updateFare(Fare fare);

    Fare findById(Long id);
}