package com.gd.orh.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Data
public class TripOrderDTO extends BaseDTO<TripOrderDTO, TripOrder> {
    private Long tripOrderId;
    private OrderStatus orderStatus;

    private Long tripId;
    private String departure;
    private Location departureLocation;
    private String destination;
    private Location destinationLocation;
    private Date createdTime;
    private Date departureTime;

    private Long passengerId;
    private Long passengerUserId;
    private String passengerUsername;
    private String passengerNickname;

    private Long driverId;
    private Long driverUserId;
    private String driverUsername;
    private String driverNickname;

    private String driverName;

    private Long carId;
    private String plateNo;
    private String brand;
    private String series;
    private String color;

    private BigDecimal lengthOfMileage;
    private Integer lengthOfTime;
    private BigDecimal MileageCost;
    private BigDecimal TimeCost;
    private BigDecimal TotalCost;

    private static class TripOrderDTOConverter extends Converter<TripOrderDTO, TripOrder> {

        @Override
        protected TripOrder doForward(TripOrderDTO tripOrderDTO) {
            TripOrder tripOrder = new TripOrder();
            BeanUtils.copyProperties(tripOrderDTO, tripOrder);

            tripOrder.setId(tripOrderDTO.getTripOrderId());

            Trip trip = new Trip();
            trip.setId(tripOrderDTO.getTripId());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                trip.setDepartureLocation(objectMapper.writeValueAsString(tripOrderDTO.getDepartureLocation()));
                trip.setDestinationLocation(objectMapper.writeValueAsString(tripOrderDTO.getDestinationLocation()));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Passenger passenger = new Passenger();
            passenger.setId(tripOrderDTO.getPassengerId());
            trip.setPassenger(passenger);

            tripOrder.setTrip(trip);

            Driver driver = new Driver();
            driver.setId(tripOrderDTO.getDriverId());
            tripOrder.setDriver(driver);

            return tripOrder;
        }

        @Override
        protected TripOrderDTO doBackward(TripOrder tripOrder) {
            TripOrderDTO tripOrderDTO = new TripOrderDTO();
            BeanUtils.copyProperties(tripOrder, tripOrderDTO);

            tripOrderDTO.setTripOrderId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getId)
                    .orElse(null)
            );

            tripOrderDTO.setTripId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getId)
                    .orElse(null)
            );

            tripOrderDTO.setDeparture(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getDeparture)
                    .orElse(null)
            );


            tripOrderDTO.setDestination(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getDestination)
                    .orElse(null)
            );

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                tripOrderDTO.setDepartureLocation(
                    objectMapper.readValue(
                        Optional.ofNullable(tripOrder)
                                .map(TripOrder::getTrip)
                                .map(Trip::getDepartureLocation)
                                .orElse(null),
                        Location.class)
                );

                tripOrderDTO.setDestinationLocation(
                    objectMapper.readValue(
                        Optional.ofNullable(tripOrder)
                                .map(TripOrder::getTrip)
                                .map(Trip::getDestinationLocation)
                                .orElse(null),
                        Location.class)
                );

            } catch (IOException e) {
                e.printStackTrace();
            }

            tripOrderDTO.setCreatedTime(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getCreatedTime)
                    .orElse(null)
            );

            tripOrderDTO.setDepartureTime(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getDepartureTime)
                    .orElse(null)
            );

            tripOrderDTO.setPassengerId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getId)
                    .orElse(null));

            tripOrderDTO.setPassengerUserId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getId)
                    .orElse(null));

            tripOrderDTO.setPassengerNickname(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            tripOrderDTO.setPassengerUsername(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getTrip)
                    .map(Trip::getPassenger)
                    .map(Passenger::getUser)
                    .map(User::getUsername)
                    .orElse(null));

            tripOrderDTO.setDriverId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getId)
                    .orElse(null)
            );

            tripOrderDTO.setDriverUserId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getUser)
                    .map(User::getId)
                    .orElse(null)
            );

            tripOrderDTO.setDriverUsername(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getUser)
                    .map(User::getUsername)
                    .orElse(null)
            );

            tripOrderDTO.setDriverNickname(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getUser)
                    .map(User::getNickname)
                    .orElse(null)
            );

            tripOrderDTO.setDriverName(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getDrivingLicense)
                    .map(DrivingLicense::getDriverName)
                    .orElse(null)
            );

            tripOrderDTO.setCarId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getVehicleLicense)
                    .map(VehicleLicense::getCar)
                    .map(Car::getId)
                    .orElse(null)
            );

            tripOrderDTO.setPlateNo(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getVehicleLicense)
                    .map(VehicleLicense::getCar)
                    .map(Car::getPlateNo)
                    .orElse(null)
            );

            tripOrderDTO.setBrand(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getVehicleLicense)
                    .map(VehicleLicense::getCar)
                    .map(Car::getBrand)
                    .orElse(null)
            );

            tripOrderDTO.setSeries(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getVehicleLicense)
                    .map(VehicleLicense::getCar)
                    .map(Car::getSeries)
                    .orElse(null)
            );

            tripOrderDTO.setColor(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getVehicleLicense)
                    .map(VehicleLicense::getCar)
                    .map(Car::getColor)
                    .orElse(null)
            );

            tripOrderDTO.setLengthOfMileage(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getLengthOfMileage)
                    .orElse(null));

            tripOrderDTO.setLengthOfTime(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getLengthOfTime)
                    .orElse(null));

            tripOrderDTO.setMileageCost(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getMileageCost)
                    .orElse(null));

            tripOrderDTO.setTimeCost(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getTimeCost)
                    .orElse(null));

            tripOrderDTO.setTotalCost(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getTotalCost)
                    .orElse(null));

            return tripOrderDTO;
        }
    }

    @Override
    protected Converter<TripOrderDTO, TripOrder> getConverter() {
        return new TripOrderDTOConverter();
    }
}
