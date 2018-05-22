package com.gd.orh.mapper;

import com.gd.orh.entity.DriverBalance;

public interface DriverBalanceMapper {
    DriverBalance findByDriverId(Long driverId);

    void updateBalance(DriverBalance driverBalance);
}