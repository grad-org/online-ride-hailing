package com.gd.orh.userMgt.service;

import com.gd.orh.entity.Driver;

public interface DriverService {
    Driver findById(Long id);

    public Driver save(Driver driver);
}
