package com.gd.orh.mapper;

import com.gd.orh.entity.DriverBalance;

public interface DriverBalanceMapper {
    void insertDriverBalance(DriverBalance driverBalance);

    void updateBalance(DriverBalance driverBalance);

    DriverBalance findById(Long id);

    void updateAlipayAccount(DriverBalance driverBalance);
}