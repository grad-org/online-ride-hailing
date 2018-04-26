package com.gd.orh.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Long tripOrderId;
    private BigDecimal totalAmount;
}
