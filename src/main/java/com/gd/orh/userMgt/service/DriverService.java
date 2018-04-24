package com.gd.orh.userMgt.service;

import com.gd.orh.entity.Driver;

import java.util.List;

public interface DriverService {
    Driver findById(Long id);

    Driver save(Driver driver);

    List<Driver> findPendingReviewDriver(Driver driver);

    List<Driver> findAllDriverByDriverStatus(Driver driver);

    Driver reviewDriver(Driver driver);
}
