package com.gd.orh.dto;

import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.Trip;
import com.gd.orh.entity.TripStatus;
import com.gd.orh.entity.TripType;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class TripDTO {
    private Long id;

    private String departure; // 出发地

    private String destination; // 目的地

    private Date createdTime; // 创建时间

    private Date departureTime; // 出发时间

    private TripType tripType; // 行程类型

    private TripStatus tripStatus; // 行程状态

    private Long passengerId;

    private static class TripDTOConverter extends Converter<TripDTO, Trip> {

        @Override
        protected Trip doForward(TripDTO tripDTO) {
            Trip trip = new Trip();
            BeanUtils.copyProperties(tripDTO, trip);

            Passenger passenger = new Passenger();
            passenger.setId(tripDTO.getPassengerId());

            trip.setPassenger(passenger);

            return trip;
        }

        @Override
        protected TripDTO doBackward(Trip trip) {
            TripDTO tripDTO = new TripDTO();
            BeanUtils.copyProperties(trip, tripDTO);

            Long passengerId =
                    trip.getPassenger() != null ? trip.getPassenger().getId() : null;
            tripDTO.setPassengerId(passengerId);

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
