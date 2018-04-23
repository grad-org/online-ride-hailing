package com.gd.orh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// 车费明细
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fare extends BaseEntity {

    private BigDecimal lengthOfMileage; // 里程数

    private Integer lengthOfTime; // 时长数(分钟)

    private FareRule fareRule; // 计费规则

    // 里程费 (总里程-起步里程)*每公里价
    public BigDecimal getMileageCost() {
        if (lengthOfMileage == null)
            return null;

        if (lengthOfMileage.compareTo(fareRule.getInitialMileage()) < 0)
            return BigDecimal.ZERO;

        return lengthOfMileage
                .subtract(fareRule.getInitialMileage())
                .multiply(fareRule.getUnitPricePerKilometer());
    }

    // 时长费 (所用时长*每分钟价)
    public BigDecimal getTimeCost() {
        if (lengthOfTime == null)
            return null;

        return BigDecimal.valueOf(lengthOfTime).multiply(fareRule.getUnitPricePerMinute());
    }

    // 总费用 (起步价+里程费+时长费)
    public BigDecimal getTotalCost() {
        if (getMileageCost() == null || getTimeCost() == null)
            return null;

        return fareRule.getInitialPrice()
                .add(getMileageCost())
                .add(getTimeCost());
    }
}
