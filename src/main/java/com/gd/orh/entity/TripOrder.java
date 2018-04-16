package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class TripOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdTime; // 创建时间

    private Date completedTime; // 完成时间

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS")
    private OrderStatus orderStatus; // 订单状态

    private Trip trip;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.ALL)
    private Driver driver;
}
