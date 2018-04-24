package com.gd.orh.mapper;

import com.gd.orh.entity.Driver;
import com.gd.orh.utils.MyMapper;

import java.util.List;

public interface DriverMapper extends MyMapper<Driver> {
    Driver findById(Long id);

    void insertDriver(Driver driver);

    List<Driver> findAllByDriverStatus(Driver driver);

    void updateDriverStatus(Driver driver);
}