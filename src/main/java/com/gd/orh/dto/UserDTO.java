package com.gd.orh.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gd.orh.entity.Driver;
import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.User;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@Data
public class UserDTO {
    private Long userId;
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

    private Long passengerId;
    private Long driverId;

    private static class UserDTOConverter extends Converter<UserDTO, User> {

        @Override
        protected User doForward(UserDTO userDTO) {
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);

            user.setId(userDTO.getUserId());

            Passenger passenger = new Passenger();
            passenger.setId(userDTO.getPassengerId());
            user.setPassenger(passenger);

            Driver driver = new Driver();
            driver.setId(userDTO.getDriverId());
            user.setDriver(driver);

            return user;
        }

        @Override
        protected UserDTO doBackward(User user) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);

            userDTO.setUserId(Optional
                    .ofNullable(user)
                    .map(User::getId)
                    .orElse(null));

            userDTO.setDriverId(Optional
                    .ofNullable(user)
                    .map(User::getDriver)
                    .map(Driver::getId)
                    .orElse(null));

            userDTO.setPassengerId(Optional
                    .ofNullable(user)
                    .map(User::getPassenger)
                    .map(Passenger::getId)
                    .orElse(null));

            return userDTO;
        }
    }

    public User convertToUser() {
        return new UserDTOConverter().convert(this);
    }

    public UserDTO convertFor(User user) {
        return new UserDTOConverter().reverse().convert(user);
    }
}
