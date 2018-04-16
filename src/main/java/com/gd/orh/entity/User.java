package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
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
    @Transient
    private MultipartFile userImage;

    @JsonIgnore
    private Boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date lastPasswordResetDate;

    @JsonIgnore
    private List<Authority> authorities;

    private Passenger passenger;

    private Driver driver;
}
