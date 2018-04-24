package com.gd.orh.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleLicense extends BaseEntity {

    private String owner; // 车辆所有人

    private Date registerDate; // 车辆注册日期

    private Driver driver;

    private Car car; // 车辆资料

}
