package com.gd.orh.mapper;

import com.gd.orh.entity.Driver;
import com.gd.orh.utils.MyMapper;

public interface DriverMapper extends MyMapper<Driver> {
    Driver findById(Long id);

    void insertDriver(Driver driver);
}