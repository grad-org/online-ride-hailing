package com.gd.orh.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Data
public class TripDTO extends BaseDTO<TripDTO, Trip> {
    private Long tripId;
    private String departure; // 出发地
    private Location departureLocation;
    private String destination; // 目的地
    private Location destinationLocation;
    private Date createdTime; // 创建时间
    private Date departureTime; // 出发时间
    private TripType tripType; // 行程类型
    private TripStatus tripStatus; // 行程状态

    private Long passengerId;
    private Long passengerUserId;
    private String passengerNickname;

    private static class TripDTOConverter extends Converter<TripDTO, Trip> {

        @Override
        protected Trip doForward(TripDTO tripDTO) {
            Trip trip = new Trip();
            BeanUtils.copyProperties(tripDTO, trip);

            trip.setId(tripDTO.getTripId());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                trip.setDepartureLocation(objectMapper.writeValueAsString(tripDTO.getDepartureLocation()));
                trip.setDestinationLocation(objectMapper.writeValueAsString(tripDTO.getDestinationLocation()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

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

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                tripDTO.setDepartureLocation(objectMapper.readValue(trip.getDepartureLocation(), Location.class));
                tripDTO.setDestinationLocation(objectMapper.readValue(trip.getDestinationLocation(), Location.class));
            } catch (IOException e) {
                e.printStackTrace();
            }

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

    @Override
    protected Converter<TripDTO, Trip> getConverter() {
        return new TripDTOConverter();
    }
}
