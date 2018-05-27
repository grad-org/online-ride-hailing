package com.gd.orh.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceRating extends BaseEntity {

    private Boolean isPassengerRated;
    private BigDecimal passengerRatingScore;
    private String passengerRatingContent;

    private Boolean isDriverRated;
    private BigDecimal driverRatingScore;
    private String driverRatingContent;

    private Long passengerId;
    private Long driverId;
    private Long tripOrderId;
}