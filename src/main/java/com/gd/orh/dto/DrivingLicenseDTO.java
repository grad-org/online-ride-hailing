package com.gd.orh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gd.orh.entity.DrivingLicense;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class DrivingLicenseDTO extends BaseDTO<DrivingLicenseDTO, DrivingLicense> {
    private Long drivingLicenseId;

    private String driverName; // 司机姓名

    private String identification; // 身份证号

    private Date issueDate; // 初次领取驾驶证日期

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String drivingLicenseImage; // 驾驶证照片

    private static class DrivingLicenseDTOConverter extends Converter<DrivingLicenseDTO, DrivingLicense> {

        @Override
        protected DrivingLicense doForward(DrivingLicenseDTO drivingLicenseDTO) {
            DrivingLicense drivingLicense = new DrivingLicense();
            BeanUtils.copyProperties(drivingLicenseDTO, drivingLicense);

            drivingLicense.setId(drivingLicenseDTO.getDrivingLicenseId());

            return drivingLicense;
        }

        @Override
        protected DrivingLicenseDTO doBackward(DrivingLicense drivingLicense) {
            DrivingLicenseDTO drivingLicenseDTO = new DrivingLicenseDTO();
            BeanUtils.copyProperties(drivingLicense, drivingLicenseDTO);

            drivingLicenseDTO.setDrivingLicenseId(drivingLicense.getId());

            return drivingLicenseDTO;
        }
    }

    @Override
    public Converter<DrivingLicenseDTO, DrivingLicense> getConverter() {
        return new DrivingLicenseDTOConverter();
    }
}
