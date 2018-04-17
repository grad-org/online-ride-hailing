package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Passenger extends BaseEntity {
    @JsonProperty
    private User user;

    @JsonProperty
    private List<Trip> trips;
}
