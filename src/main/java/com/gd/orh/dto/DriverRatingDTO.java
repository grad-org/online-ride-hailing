package com.gd.orh.dto;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.DriverRating;
import com.gd.orh.entity.User;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Data
public class DriverRatingDTO extends BaseDTO<DriverRatingDTO, DriverRating> {
    private Long driverRatingId;

    private Double ratingScore;

    private Long driverId;

    private Long driverUserId;

    private String nickname;

    private static class DriverRatingDTOConverter extends Converter<DriverRatingDTO, DriverRating> {

        @Override
        protected DriverRating doForward(DriverRatingDTO driverRatingDTO) {
            DriverRating driverRating = new DriverRating();
            BeanUtils.copyProperties(driverRatingDTO, driverRating);

            User user = new User();
            user.setId(driverRatingDTO.getDriverUserId());
            user.setNickname(driverRatingDTO.getNickname());

            Driver driver = new Driver();
            driver.setId(driverRatingDTO.getDriverId());
            driver.setUser(user);

            driverRating.setDriver(driver);

            return driverRating;
        }

        @Override
        protected DriverRatingDTO doBackward(DriverRating driverRating) {
            DriverRatingDTO driverRatingDTO = new DriverRatingDTO();
            BeanUtils.copyProperties(driverRating, driverRatingDTO);

            driverRatingDTO.setDriverRatingId(Optional
                    .ofNullable(driverRating)
                    .map(DriverRating::getId)
                    .orElse(null));

            driverRatingDTO.setDriverId(Optional
                    .ofNullable(driverRating)
                    .map(DriverRating::getDriver)
                    .map(Driver::getId)
                    .orElse(null));

            driverRatingDTO.setDriverUserId(Optional
                    .ofNullable(driverRating)
                    .map(DriverRating::getDriver)
                    .map(Driver::getUser)
                    .map(User::getId)
                    .orElse(null));

            driverRatingDTO.setNickname(Optional
                    .ofNullable(driverRating)
                    .map(DriverRating::getDriver)
                    .map(Driver::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            return driverRatingDTO;
        }
    }

    @Override
    public Converter<DriverRatingDTO, DriverRating> getConverter() {
        return new DriverRatingDTOConverter();
    }
}
