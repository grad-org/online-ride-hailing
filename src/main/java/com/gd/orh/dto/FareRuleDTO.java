package com.gd.orh.dto;

import com.gd.orh.entity.FareRule;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FareRuleDTO {
    private Long fareRuleId;

    private BigDecimal initialPrice; // 起步价

    private BigDecimal initialMileage; // 起步里程

    private BigDecimal unitPricePerKilometer; // 每公里价

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

    public FareRule convertToFareRule() {
        return new FareRuleDTOConverter().convert(this);
    }

    public FareRuleDTO convertFor(FareRule fareRule) {
        return new FareRuleDTOConverter().reverse().convert(fareRule);
    }
}
