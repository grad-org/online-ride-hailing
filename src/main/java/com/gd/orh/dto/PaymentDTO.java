package com.gd.orh.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class PaymentDTO {
    @NotNull
    private Long tripOrderId;
    private BigDecimal totalAmount;
}
