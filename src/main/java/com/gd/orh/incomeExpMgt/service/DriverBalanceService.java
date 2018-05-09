package com.gd.orh.incomeExpMgt.service;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.DriverBalance;

import java.math.BigDecimal;

public interface DriverBalanceService {
    DriverBalance findByDriverId(Long driverId);

    void deposit(Driver driver, BigDecimal amount);

    void withdraw(Driver driver, BigDecimal amount);

    DriverBalance withdraw(DriverBalance driverBalance, BigDecimal amount);
}
