package com.gd.orh.entity;

import lombok.Data;

import java.util.Date;

// 听单条件
@Data
public class ListeningOrderCondition {
    private TripType tripType;
    private Date startDate;
    private Date endDate;
    private Date departureTime;

    private Integer page = 1;
    private Integer rows = 10;
}
