package com.gd.orh.entity;

import lombok.Data;

@Data
public class DriverComplaint extends BaseEntity {
    private String complaintContent;

    private Driver driver;

    private Long tripOrderId;
}