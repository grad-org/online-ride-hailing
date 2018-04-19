package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Driver;
import com.gd.orh.mapper.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverMapper driverMapper;

    @Override
    @Transactional(readOnly = true)
    public Driver findById(Long id) {
            return driverMapper.findById(id);
    }
}
