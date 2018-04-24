package com.gd.orh.dto;

import com.gd.orh.entity.Car;
import com.gd.orh.entity.CarLocation;
import com.gd.orh.entity.Location;
import com.google.common.base.Converter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarLocationDTO extends BaseDTO<CarLocationDTO, CarLocation> {
    private Long carId;

    private String lng; // 经度

    private String lat; // 纬度

    private static class CarLocationDTOConverter extends Converter<CarLocationDTO, CarLocation> {

        @Override
        protected CarLocation doForward(CarLocationDTO carLocationDTO) {
            CarLocation carLocation = new CarLocation();

            Car car = new Car();
            car.setId(carLocationDTO.getCarId());
            carLocation.setCar(car);

            Location location = new Location(carLocationDTO.getLng(), carLocationDTO.getLat());
            carLocation.setLocation(location);

            return carLocation;
        }

        @Override
        protected CarLocationDTO doBackward(CarLocation carLocation) {
            Long carId = Optional
                    .ofNullable(carLocation)
                    .map(CarLocation::getCar)
                    .map(Car::getId)
                    .orElse(null);

            String lng = Optional
                    .ofNullable(carLocation)
                    .map(CarLocation::getLocation)
                    .map(Location::getLng)
                    .orElse(null);

            String lat = Optional
                    .ofNullable(carLocation)
                    .map(CarLocation::getLocation)
                    .map(Location::getLat)
                    .orElse(null);


            CarLocationDTO carLocationDTO = new CarLocationDTO(carId, lng, lat);

            return carLocationDTO;
        }
    }

    @Override
    public Converter<CarLocationDTO, CarLocation> getConverter() {
        return new CarLocationDTOConverter();
    }
}
