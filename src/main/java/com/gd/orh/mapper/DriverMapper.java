package com.gd.orh.mapper;

import com.gd.orh.entity.Driver;

import java.util.List;

public interface DriverMapper {
    Driver findById(Long id);

    void insertDriver(Driver driver);

    List<Driver> findAllByDriverStatus(Driver driver);

    void updateDriverStatus(Driver driver);

    void updateDriver(Driver driver);
}