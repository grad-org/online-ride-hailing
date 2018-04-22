package com.gd.orh.mapper;

import com.gd.orh.entity.Passenger;
import com.gd.orh.utils.MyMapper;

public interface PassengerMapper extends MyMapper<Passenger> {
    Passenger findById(Long id);

    void insertPassenger(Passenger passenger);
}