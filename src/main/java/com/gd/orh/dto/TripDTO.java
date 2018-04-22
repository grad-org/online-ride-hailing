package com.gd.orh.dto;

import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.Optional;

@Data
public class TripDTO {
    private Long tripId;
    private String departure; // 出发地
    private String destination; // 目的地
    private Date createdTime; // 创建时间
    private Date departureTime; // 出发时间
    private TripType tripType; // 行程类型
    private TripStatus tripStatus; // 行程状态

    private Long passengerId;
    private Long passengerUserId;
    private String passengerNickname;

    private Location departureLocation;

    private static class TripDTOConverter extends Converter<TripDTO, Trip> {

        @Override
        protected Trip doForward(TripDTO tripDTO) {
            Trip trip = new Trip();
            BeanUtils.copyProperties(tripDTO, trip);

            trip.setId(tripDTO.getTripId());

            Passenger passenger = new Passenger();
            passenger.setId(tripDTO.getPassengerId());

            trip.setPassenger(passenger);

            return trip;
        }

        @Override
        protected TripDTO doBackward(Trip trip) {
            TripDTO tripDTO = new TripDTO();
            BeanUtils.copyProperties(trip, tripDTO);


            tripDTO.setTripId(Optional
                    .ofNullable(trip)
                    .map(Trip::getId)
                    .orElse(null)
            );

            tripDTO.setPassengerId(Optional
                    .ofNullable(trip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getId)
                    .orElse(null)
            );

            tripDTO.setPassengerUserId(Optional
                    .ofNullable(trip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getId)
                    .orElse(null)
            );

            tripDTO.setPassengerNickname(Optional
                    .ofNullable(trip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            return tripDTO;
        }
    }

    public Trip convertToTrip() {
        return new TripDTOConverter().convert(this);
    }

    public TripDTO convertFor(Trip trip) {

        return new TripDTOConverter().reverse().convert(trip);
    }
}
