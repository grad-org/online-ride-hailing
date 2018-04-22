package com.gd.orh.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TripOrderDTO {
    private Long tripOrderId;
    private Long tripId;
    private Long driverId;
    private Long fareRuleId;
    private BigDecimal lengthOfMileage;
    private Integer lengthOfTime;
}
