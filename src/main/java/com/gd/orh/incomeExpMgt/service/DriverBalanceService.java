package com.gd.orh.incomeExpMgt.service;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.DriverBalance;
import com.gd.orh.mapper.DriverBalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;

@Transactional
@Service
public class DriverBalanceService {

    @Autowired
    private DriverBalanceMapper driverBalanceMapper;

    @Transactional(readOnly = true)
    public DriverBalance findByDriverId(Long driverId) {
        Example example = new Example(DriverBalance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("driverId", driverId);

        return driverBalanceMapper.selectOneByExample(example);
    }

    public void deposit(Driver driver, BigDecimal amount) {
        DriverBalance driverBalance = findByDriverId(driver.getId());

        driverBalance.setBalance(driverBalance.getBalance().add(amount));

        driverBalanceMapper.updateByPrimaryKey(driverBalance);
    }

    public void withdraw(Driver driver, BigDecimal amount) {
        DriverBalance driverBalance = findByDriverId(driver.getId());

        driverBalance.setBalance(driverBalance.getBalance().subtract(amount));

        driverBalanceMapper.updateByPrimaryKey(driverBalance);
    }

    public DriverBalance withdraw(DriverBalance driverBalance, BigDecimal amount) {
        driverBalance.setBalance(driverBalance.getBalance().subtract(amount));

        driverBalanceMapper.updateByPrimaryKey(driverBalance);

        return driverBalance;
    }
}
