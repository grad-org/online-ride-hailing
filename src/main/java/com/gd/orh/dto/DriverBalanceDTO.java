package com.gd.orh.dto;

import com.gd.orh.entity.DriverBalance;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
public class DriverBalanceDTO extends BaseDTO<DriverBalanceDTO, DriverBalance> {
    private Long driverBalanceId;

    private String alipayAccount;

    private BigDecimal balance;

    private Long driverId;

    private static class DriverBalanceDTOConverter extends Converter<DriverBalanceDTO, DriverBalance> {

        @Override
        protected DriverBalance doForward(DriverBalanceDTO driverBalanceDTO) {
            DriverBalance driverBalance = new DriverBalance();
            BeanUtils.copyProperties(driverBalanceDTO, driverBalance);

            driverBalance.setId(driverBalanceDTO.getDriverBalanceId());

            return driverBalance;
        }

        @Override
        protected DriverBalanceDTO doBackward(DriverBalance driverBalance) {
            DriverBalanceDTO driverBalanceDTO = new DriverBalanceDTO();
            BeanUtils.copyProperties(driverBalance, driverBalanceDTO);

            driverBalanceDTO.setDriverBalanceId(driverBalance.getId());

            return driverBalanceDTO;
        }
    }

    @Override
    public Converter<DriverBalanceDTO, DriverBalance> getConverter() {
        return new DriverBalanceDTOConverter();
    }
}
