package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Fare;

public interface FareService {

    Fare findById(Long id);

    Fare predictFare(Fare fare);
}
