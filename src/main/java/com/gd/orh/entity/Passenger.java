package com.gd.orh.entity;

import lombok.Data;

import java.util.List;

@Data
public class Passenger extends BaseEntity {
    private User user;

    private List<Trip> trips;
}
