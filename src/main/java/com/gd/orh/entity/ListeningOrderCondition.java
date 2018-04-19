package com.gd.orh.entity;

import lombok.Data;

import java.util.Date;

// 听单条件
@Data
public class ListeningOrderCondition extends BaseEntity {
    private TripType tripType;
    private Date startDate;
    private Date endDate;
    private Date departureTime;
}
