package com.gd.orh.dto;

import lombok.Data;

@Data
public class TripOrderDTO {
    private Long tripOrderId;
    private Long tripId;
    private Long driverId;
    private Long fareRuleId;
    private Long lengthOfMileage;
    private Long lengthOfTime;
}
