package com.gd.orh.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class VehicleLicense extends BaseEntity {

    private MultipartFile vehicleLicenseImage; // 行驶证照片

    private String owner; // 车辆所有人

    private Date registerDate; // 车辆注册日期

    private Driver driver;

    private Car car; // 车辆资料

}
