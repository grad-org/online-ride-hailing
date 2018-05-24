package com.gd.orh.incomeExpMgt.service;

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
    public DriverBalance findById(Long id) {
        return driverBalanceMapper.findById(id);
    }

    @Override
    public DriverBalance insertDriverBalance(DriverBalance driverBalance) {
        driverBalance.setAlipayAccount(null);
        driverBalance.setBalance(new BigDecimal("0"));

        driverBalanceMapper.insertDriverBalance(driverBalance);
        return driverBalance;
    }

    @Override
    public DriverBalance bindAlipayAccount(DriverBalance driverBalance) {
        driverBalanceMapper.updateAlipayAccount(driverBalance);

        return this.findById(driverBalance.getId());
    }

    @Override
    public DriverBalance deposit(DriverBalance driverBalance, BigDecimal amount) {
        driverBalance.setBalance(driverBalance.getBalance().add(amount));

        driverBalanceMapper.updateBalance(driverBalance);

        return driverBalance;
    }

    @Override
    public DriverBalance withdraw(DriverBalance driverBalance, BigDecimal amount) {
        driverBalance.setBalance(driverBalance.getBalance().subtract(amount));

        driverBalanceMapper.updateBalance(driverBalance);

        return driverBalance;
    }
}
