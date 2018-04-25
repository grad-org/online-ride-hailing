package com.gd.orh.entity;

import lombok.Data;

@Data
public class PassengerRating extends BaseEntity {
    private Double ratingScore;

    private Passenger passenger;

    private Long tripOrderId;
}