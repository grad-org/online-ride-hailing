package com.gd.orh.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Trip extends BaseEntity {
    private String departure; // 出发地

    private String destination; // 目的地

    private Date createdTime; // 创建时间

    private Date departureTime; // 出发时间

    private TripStatus tripStatus; // 行程状态

    private Passenger passenger;
}
