package com.gd.orh.dto;

import com.gd.orh.entity.ServiceRating;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Data
public class ServiceRatingDTO extends BaseDTO<ServiceRatingDTO, ServiceRating> {
    private Long serviceRatingId;

    private Boolean isPassengerRated;
    private BigDecimal passengerRatingScore;
    private String passengerRatingContent;

    private Boolean isDriverRated;
    private BigDecimal driverRatingScore;
    private String driverRatingContent;

    private Long passengerId;
    private Long driverId;
    private Long tripOrderId;

    private static class ServiceRatingDTOConverter extends Converter<ServiceRatingDTO, ServiceRating> {

        @Override
        protected ServiceRating doForward(ServiceRatingDTO serviceRatingDTO) {
            ServiceRating serviceRating = new ServiceRating();
            BeanUtils.copyProperties(serviceRatingDTO, serviceRating);

            serviceRating.setId(serviceRatingDTO.getServiceRatingId());

            return serviceRating;
        }

        @Override
        protected ServiceRatingDTO doBackward(ServiceRating serviceRating) {
            ServiceRatingDTO serviceRatingDTO = new ServiceRatingDTO();
            BeanUtils.copyProperties(serviceRating, serviceRatingDTO);

            serviceRatingDTO.setServiceRatingId(Optional
                    .ofNullable(serviceRating)
                    .map(ServiceRating::getId)
                    .orElse(null));

            return serviceRatingDTO;
        }
    }

    @Override
    public Converter<ServiceRatingDTO, ServiceRating> getConverter() {
        return new ServiceRatingDTOConverter();
    }
}
