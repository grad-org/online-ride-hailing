package com.gd.orh.incomeExpMgt.service;

import com.gd.orh.entity.DriverBalance;

import java.math.BigDecimal;

public interface DriverBalanceService {
    DriverBalance insertDriverBalance(DriverBalance driverBalance);

    DriverBalance findById(Long id);

    DriverBalance bindAlipayAccount(DriverBalance driverBalance);

    DriverBalance deposit(DriverBalance driverBalance, BigDecimal amount);

    DriverBalance withdraw(DriverBalance driverBalance, BigDecimal amount);
}
