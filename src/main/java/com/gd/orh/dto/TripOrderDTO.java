package com.gd.orh.dto;

import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Data
public class TripOrderDTO {
    private Long tripOrderId;
    private OrderStatus orderStatus;

    private Long tripId;
    private String departure;
    private String destination;

    private Long driverId;
    private Long driverUserId;
    private String driverNickname;

    private Long carId;
    private String plateNo;
    private String brand;
    private String series;
    private String color;

    private Location departureLocation;

    private Long fareRuleId;

    private BigDecimal lengthOfMileage;
    private Integer lengthOfTime;

    private static class TripOrderDTOConverter extends Converter<TripOrderDTO, TripOrder> {

        @Override
        protected TripOrder doForward(TripOrderDTO tripOrderDTO) {
            TripOrder tripOrder = new TripOrder();
            BeanUtils.copyProperties(tripOrderDTO, tripOrder);

            tripOrder.setId(tripOrderDTO.getTripOrderId());

            Trip trip = new Trip();
            trip.setId(tripOrderDTO.getTripId());
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

            tripOrderDTO.setDriverNickname(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getDriver)
                    .map(Driver::getUser)
                    .map(User::getNickname)
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

            tripOrderDTO.setFareRuleId(Optional
                    .ofNullable(tripOrder)
                    .map(TripOrder::getFare)
                    .map(Fare::getFareRule)
                    .map(FareRule::getId)
                    .orElse(null)
            );

            return tripOrderDTO;
        }
    }

    public TripOrder convertToTripOrder() {
        return new TripOrderDTOConverter().convert(this);
    }

    public TripOrderDTO convertFor(TripOrder tripOrder) {

        return new TripOrderDTOConverter().reverse().convert(tripOrder);
    }
}
