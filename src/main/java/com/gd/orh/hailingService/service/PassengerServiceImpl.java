package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Passenger;
import com.gd.orh.mapper.PassengerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerMapper passengerMapper;

    @Override
    @Transactional(readOnly = true)
    public Passenger findById(Long id) {
        return passengerMapper.findById(id);
    }
}
