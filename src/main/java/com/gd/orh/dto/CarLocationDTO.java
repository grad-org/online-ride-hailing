package com.gd.orh.dto;

import com.gd.orh.entity.Car;
import com.gd.orh.entity.CarLocation;
import com.gd.orh.entity.Location;
import com.google.common.base.Converter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarLocationDTO {
    private Long cardId;

    private String lng; // 经度

    private String lat; // 纬度

    private static class CarLocationDTOConverter extends Converter<CarLocationDTO, CarLocation> {

        @Override
        protected CarLocation doForward(CarLocationDTO carLocationDTO) {
            CarLocation carLocation = new CarLocation();

            Car car = new Car();
            car.setId(carLocationDTO.getCardId());
            carLocation.setCar(car);

            Location location = new Location(carLocationDTO.getLng(), carLocationDTO.getLat());
            carLocation.setLocation(location);

            return carLocation;
        }

        @Override
        protected CarLocationDTO doBackward(CarLocation carLocation) {

            Long cardId =
                    carLocation.getCar() != null ? carLocation.getCar().getId() : null;
            String lng =
                    carLocation.getLocation() != null ? carLocation.getLocation().getLng() : null;
            String lat =
                    carLocation.getLocation() != null ? carLocation.getLocation().getLat() : null;

            CarLocationDTO carLocationDTO = new CarLocationDTO(cardId, lng, lat);

            return carLocationDTO;
        }
    }

    public CarLocation convertToCarLocation() {
        return new CarLocationDTOConverter().convert(this);
    }

    public CarLocationDTO convertFor(CarLocation carLocation) {
        return new CarLocationDTOConverter().reverse().convert(carLocation);
    }
}
