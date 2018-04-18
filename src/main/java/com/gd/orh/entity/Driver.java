package com.gd.orh.entity;

import lombok.Data;

import java.util.List;

@Data
public class Driver extends BaseEntity {
    private User user;

    private List<TripOrder> tripOrders;
}
