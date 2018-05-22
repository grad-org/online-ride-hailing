package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
public class User extends BaseEntity {

    @NotEmpty
    private String username;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    private String gender;

    private Integer age;

    @JsonIgnore
    private Boolean enabled;

    @JsonIgnore
    private Date lastPasswordResetDate;

    @JsonIgnore
    private List<Authority> authorities;

    private Passenger passenger;

    private Driver driver;
}
