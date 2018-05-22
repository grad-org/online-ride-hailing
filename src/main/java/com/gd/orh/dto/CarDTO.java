package com.gd.orh.dto;

import com.gd.orh.entity.Car;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CarDTO extends BaseDTO<CarDTO, Car> {
    private Long carId;

    private String plateNo; // 车牌号

    private String brand; // 品牌

    private String series; // 系列

    private String color; // 颜色

    private static class CarDTOConverter extends Converter<CarDTO, Car> {

        @Override
        protected Car doForward(CarDTO carDTO) {
            Car car = new Car();
            BeanUtils.copyProperties(carDTO, car);

            car.setId(carDTO.getCarId());

            return car;
        }

        @Override
        protected CarDTO doBackward(Car car) {
            CarDTO carDTO = new CarDTO();
            BeanUtils.copyProperties(car, carDTO);

            carDTO.setCarId(car.getId());

            return carDTO;
        }
    }

    @Override
    public Converter<CarDTO, Car> getConverter() {
        return new CarDTOConverter();
    }
}
