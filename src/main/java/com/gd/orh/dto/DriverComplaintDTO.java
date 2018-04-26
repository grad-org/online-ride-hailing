package com.gd.orh.dto;

import com.gd.orh.entity.Driver;
import com.gd.orh.entity.DriverComplaint;
import com.gd.orh.entity.User;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Data
public class DriverComplaintDTO extends BaseDTO<DriverComplaintDTO, DriverComplaint> {
    private Long driverComplaintId;

    private String complaintContent;

    private Long driverId;

    private Long driverUserId;

    private String nickname;

    private static class DriverComplaintDTOConverter extends Converter<DriverComplaintDTO, DriverComplaint> {

        @Override
        protected DriverComplaint doForward(DriverComplaintDTO driverComplaintDTO) {
            DriverComplaint driverComplaint = new DriverComplaint();
            BeanUtils.copyProperties(driverComplaintDTO, driverComplaint);

            User user = new User();
            user.setId(driverComplaintDTO.getDriverUserId());
            user.setNickname(driverComplaintDTO.getNickname());

            Driver driver = new Driver();
            driver.setId(driverComplaintDTO.getDriverId());
            driver.setUser(user);

            driverComplaint.setDriver(driver);

            return driverComplaint;
        }

        @Override
        protected DriverComplaintDTO doBackward(DriverComplaint driverComplaint) {
            DriverComplaintDTO driverComplaintDTO = new DriverComplaintDTO();
            BeanUtils.copyProperties(driverComplaint, driverComplaintDTO);

            driverComplaintDTO.setDriverComplaintId(Optional
                    .ofNullable(driverComplaint)
                    .map(DriverComplaint::getId)
                    .orElse(null));

            driverComplaintDTO.setDriverId(Optional
                    .ofNullable(driverComplaint)
                    .map(DriverComplaint::getDriver)
                    .map(Driver::getId)
                    .orElse(null));

            driverComplaintDTO.setDriverUserId(Optional
                    .ofNullable(driverComplaint)
                    .map(DriverComplaint::getDriver)
                    .map(Driver::getUser)
                    .map(User::getId)
                    .orElse(null));

            driverComplaintDTO.setNickname(Optional
                    .ofNullable(driverComplaint)
                    .map(DriverComplaint::getDriver)
                    .map(Driver::getUser)
                    .map(User::getNickname)
                    .orElse(null));

            return driverComplaintDTO;
        }
    }

    @Override
    public Converter<DriverComplaintDTO, DriverComplaint> getConverter() {
        return new DriverComplaintDTOConverter();
    }
}
