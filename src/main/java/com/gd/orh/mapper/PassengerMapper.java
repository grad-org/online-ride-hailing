package com.gd.orh.mapper;

import com.gd.orh.entity.Passenger;

public interface PassengerMapper {
    Passenger findById(Long id);

    void insertPassenger(Passenger passenger);
}