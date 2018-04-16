package com.gd.orh.hailingService.service;

import com.gd.orh.entity.Passenger;
import com.gd.orh.hailingService.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    @Transactional(readOnly = true)
    public Passenger findById(Long id) {
        return passengerRepository.getOne(id);
    }
}
