package com.gd.orh.entity;

import lombok.Data;

@Data
public class PassengerComplaint extends BaseEntity {
    private String complaintContent;

    private Passenger passenger;

    private Long tripOrderId;
}