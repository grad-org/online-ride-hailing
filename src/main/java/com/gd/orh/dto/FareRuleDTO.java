package com.gd.orh.dto;

import com.gd.orh.entity.FareRule;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FareRuleDTO extends BaseDTO<FareRuleDTO, FareRule> {
    private Long fareRuleId;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal initialPrice; // 起步价

    @NotNull
    @Min(value = 0)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal initialMileage; // 起步里程

    @NotNull
    @Min(value = 0)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal unitPricePerKilometer; // 每公里价

    @NotNull
    @Min(value = 0)
    @Digits(integer = 2, fraction = 2)
    private BigDecimal unitPricePerMinute; // 每分钟价

    private Date setupTime; // 设置时间

    private static class FareRuleDTOConverter extends Converter<FareRuleDTO, FareRule> {

        @Override
        protected FareRule doForward(FareRuleDTO fareRuleDTO) {
            FareRule fareRule = new FareRule();
            BeanUtils.copyProperties(fareRuleDTO, fareRule);

            fareRule.setId(fareRuleDTO.getFareRuleId());

            return fareRule;
        }

        @Override
        protected FareRuleDTO doBackward(FareRule fareRule) {
            FareRuleDTO fareRuleDTO = new FareRuleDTO();
            BeanUtils.copyProperties(fareRule, fareRuleDTO);

            fareRuleDTO.setFareRuleId(fareRule.getId());

            return fareRuleDTO;
        }
    }

    @Override
    protected Converter<FareRuleDTO, FareRule> getConverter() {
        return new FareRuleDTOConverter();
    }
}
