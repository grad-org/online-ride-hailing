package com.gd.orh.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
public class User extends BaseEntity {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String nickname;

    private String gender;

    private Integer age;

    private Boolean enabled;

    private Date lastPasswordResetDate;

    private List<Authority> authorities;

    private Passenger passenger;

    private Driver driver;
}
