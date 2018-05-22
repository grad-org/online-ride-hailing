package com.gd.orh.entity;

import lombok.Data;

@Data
public class Car extends BaseEntity {

    private String plateNo; // 车牌号

    private String brand; // 品牌

    private String series; // 系列

    private String color; // 颜色

    private VehicleLicense vehicleLicense; // 行驶证信息
}
