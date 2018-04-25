package com.gd.orh.entity;

import lombok.Data;

@Data
public class DriverRating extends BaseEntity {
    private Double ratingScore;

    private Driver driver;

    private Long tripOrderId;
}