package com.gd.orh.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DriverBalance extends BaseEntity {
    private String alipayAccount;

    private BigDecimal balance;

    private Long driverId;
}