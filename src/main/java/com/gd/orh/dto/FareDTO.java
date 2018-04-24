package com.gd.orh.dto;

import com.gd.orh.entity.Fare;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;

@Data
public class FareDTO extends BaseDTO<FareDTO, Fare> {
    private Long fareId;

    @NotNull
    @Min(value = 0)
    private BigDecimal lengthOfMileage; // 里程数

    @NotNull
    @Min(value = 0)
    private Integer lengthOfTime; // 时长数(分钟)

    // 里程费 (总里程-起步里程)*每公里价
    public BigDecimal mileageCost;

    // 时长费 (所用时长*每分钟价)
    public BigDecimal timeCost;

    // 总费用 (起步价+里程费+时长费)
    public BigDecimal totalCost;

    private FareRuleDTO fareRuleDTO; // 计费规则

    private static class FareDTOConverter extends Converter<FareDTO, Fare> {
        @Override
        protected Fare doForward(FareDTO fareDTO) {
            Fare fare = new Fare();
            BeanUtils.copyProperties(fareDTO, fare);

            fare.setId(fareDTO.getFareId());

            fare.setFareRule(Optional
                    .ofNullable(fareDTO)
                    .map(FareDTO::getFareRuleDTO)
                    .map(FareRuleDTO::convertTo)
                    .orElse(null));

            return fare;
        }

        @Override
        protected FareDTO doBackward(Fare fare) {
            FareDTO fareDTO = new FareDTO();
            BeanUtils.copyProperties(fare, fareDTO);

            fareDTO.setFareId(fare.getId());

            if (fare.getFareRule() != null)
                fareDTO.setFareRuleDTO(new FareRuleDTO().convertFor(fare.getFareRule()));

            return fareDTO;
        }
    }

    @Override
    protected Converter<FareDTO, Fare> getConverter() {
        return new FareDTOConverter();
    }
}
