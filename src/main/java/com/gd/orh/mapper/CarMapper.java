package com.gd.orh.mapper;

import com.gd.orh.entity.Car;
import com.gd.orh.utils.MyMapper;

public interface CarMapper extends MyMapper<Car> {
    void insertCar(Car car);
}