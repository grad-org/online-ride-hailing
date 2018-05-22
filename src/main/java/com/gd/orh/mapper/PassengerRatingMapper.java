package com.gd.orh.mapper;

import com.gd.orh.entity.PassengerRating;

public interface PassengerRatingMapper {
    void insertPassengerRating(PassengerRating passengerRating);

    PassengerRating findById(Long id);
}