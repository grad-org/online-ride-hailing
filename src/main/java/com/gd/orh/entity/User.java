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
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @JsonIgnore
    private Date lastPasswordResetDate;

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    @JsonIgnore
    private List<Authority> authorities;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Passenger passenger;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Driver driver;
}
