package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departure; // 出发地

    private String destination; // 目的地

    private Date createdTime; // 创建时间

    private Date departureTime; // 出发时间

    @Enumerated(EnumType.STRING)
    @Column(name = "TRIP_STATUS")
    private TripStatus tripStatus; // 行程状态

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.ALL)
    private Passenger passenger;
}
