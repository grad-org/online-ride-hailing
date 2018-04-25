package com.gd.orh.dto;

import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Data
public class PassengerRatingDTO extends BaseDTO<PassengerRatingDTO, PassengerRating> {
    private Long passengerRatingId;

    private Double ratingScore;

    private Long passengerId;

    private Long passengerUserId;

    private String nickname;

    private static class PassengerRatingDTOConverter extends Converter<PassengerRatingDTO, PassengerRating> {

        @Override
        protected PassengerRating doForward(PassengerRatingDTO passengerRatingDTO) {
            PassengerRating passengerRating = new PassengerRating();
            BeanUtils.copyProperties(passengerRatingDTO, passengerRating);

            User user = new User();
            user.setId(passengerRatingDTO.getPassengerUserId());
            user.setNickname(passengerRatingDTO.getNickname());

            Passenger passenger = new Passenger();
            passenger.setId(passengerRatingDTO.getPassengerId());
            passenger.setUser(user);

            passengerRating.setPassenger(passenger);

            return passengerRating;
        }

        @Override
        protected PassengerRatingDTO doBackward(PassengerRating passengerRating) {
            PassengerRatingDTO passengerRatingDTO = new PassengerRatingDTO();
            BeanUtils.copyProperties(passengerRating, passengerRatingDTO);

            passengerRatingDTO.setPassengerRatingId(Optional
                    .ofNullable(passengerRating)
                    .map(PassengerRating::getId)
                    .orElse(null));

            passengerRatingDTO.setPassengerId(Optional
                    .ofNullable(passengerRating)
                    .map(PassengerRating::getPassenger)
                    .map(Passenger::getId)
                    .orElse(null));

            passengerRatingDTO.setPassengerUserId(Optional
                    .ofNullable(passengerRating)
                    .map(PassengerRating::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getId)
                    .orElse(null));

            passengerRatingDTO.setNickname(Optional
                    .ofNullable(passengerRating)
                    .map(PassengerRating::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            return passengerRatingDTO;
        }
    }

    @Override
    public Converter<PassengerRatingDTO, PassengerRating> getConverter() {
        return new PassengerRatingDTOConverter();
    }
}
