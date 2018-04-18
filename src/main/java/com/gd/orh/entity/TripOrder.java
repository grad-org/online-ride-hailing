package com.gd.orh.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TripOrder extends BaseEntity {

    private Date acceptedTime; // 受理时间

    private Date completedTime; // 完成时间

    private OrderStatus orderStatus; // 订单状态

    private Trip trip; // 行程

    private Driver driver; // 乘客资料
}
