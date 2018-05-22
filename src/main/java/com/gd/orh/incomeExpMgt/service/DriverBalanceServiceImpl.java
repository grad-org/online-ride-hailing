package com.gd.orh.incomeExpMgt.service;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.DriverBalance;
import com.gd.orh.mapper.DriverBalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@Service
public class DriverBalanceServiceImpl implements DriverBalanceService {

    @Autowired
    private DriverBalanceMapper driverBalanceMapper;

    @Override
    @Transactional(readOnly = true)
    public DriverBalance findByDriverId(Long driverId) {
        return driverBalanceMapper.findByDriverId(driverId);
    }

    @Override
    public void deposit(Driver driver, BigDecimal amount) {
        DriverBalance driverBalance = findByDriverId(driver.getId());

        driverBalance.setBalance(driverBalance.getBalance().add(amount));

        driverBalanceMapper.updateBalance(driverBalance);
    }

    @Override
    public void withdraw(Driver driver, BigDecimal amount) {
        DriverBalance driverBalance = findByDriverId(driver.getId());

        driverBalance.setBalance(driverBalance.getBalance().subtract(amount));

        driverBalanceMapper.updateBalance(driverBalance);
    }

    @Override
    public DriverBalance withdraw(DriverBalance driverBalance, BigDecimal amount) {
        driverBalance.setBalance(driverBalance.getBalance().subtract(amount));

        driverBalanceMapper.updateBalance(driverBalance);

        return driverBalance;
    }
}
