package com.gd.orh.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawalDTO {
    private Long driverBalanceId;
    private BigDecimal amountOfWithdrawal;
}
