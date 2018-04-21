package com.gd.orh.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FareRule extends BaseEntity {

    private BigDecimal initialPrice; // 起步价

    private BigDecimal initialMileage; // 起步里程

    private BigDecimal unitPricePerKilometer; // 每公里价

    private BigDecimal unitPricePerMinute; // 每分钟价

    private Date setupTime; // 设置时间
}
