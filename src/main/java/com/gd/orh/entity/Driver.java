package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
}
