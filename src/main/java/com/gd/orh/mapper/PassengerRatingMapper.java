package com.gd.orh.mapper;

import com.gd.orh.entity.PassengerRating;
import com.gd.orh.utils.MyMapper;

public interface PassengerRatingMapper extends MyMapper<PassengerRating> {
    void insertPassengerRating(PassengerRating passengerRating);

    PassengerRating findById(Long id);
}