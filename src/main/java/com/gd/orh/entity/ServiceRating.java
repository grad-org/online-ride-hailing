package com.gd.orh.entity;

import lombok.Data;

@Data
public class ServiceRating extends BaseEntity {

    private Boolean isPassengerRated;
    private String passengerRatingScore;
    private String passengerRatingContent;

    private Boolean isDriverRated;
    private String driverRatingScore;
    private String driverRatingContent;

    private Long passengerId;
    private Long driverId;
    private Long tripOrderId;
}