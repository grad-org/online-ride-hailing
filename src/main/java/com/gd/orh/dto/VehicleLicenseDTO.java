package com.gd.orh.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gd.orh.entity.VehicleLicense;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class VehicleLicenseDTO extends BaseDTO<VehicleLicenseDTO, VehicleLicense> {

    private Long vehicleLicenseId;

    private String owner; // 车辆所有人

    private Date registerDate; // 车辆注册日期

    @JsonIgnore
    private MultipartFile vehicleLicenseImage; // 行驶证照片

    private static class VehicleLicenseDTOConverter extends Converter<VehicleLicenseDTO, VehicleLicense> {

        @Override
        protected VehicleLicense doForward(VehicleLicenseDTO vehicleLicenseDTO) {
            VehicleLicense vehicleLicense = new VehicleLicense();
            BeanUtils.copyProperties(vehicleLicenseDTO, vehicleLicense);

            vehicleLicense.setId(vehicleLicenseDTO.getVehicleLicenseId());

            return vehicleLicense;
        }

        @Override
        protected VehicleLicenseDTO doBackward(VehicleLicense vehicleLicense) {
            VehicleLicenseDTO vehicleLicenseDTO = new VehicleLicenseDTO();
            BeanUtils.copyProperties(vehicleLicense, vehicleLicenseDTO);

            vehicleLicenseDTO.setVehicleLicenseId(vehicleLicense.getId());

            return vehicleLicenseDTO;
        }
    }

    public VehicleLicense convertToVehicleLicense() {
        return new VehicleLicenseDTOConverter().convert(this);
    }

    public VehicleLicenseDTO convertFor(VehicleLicense vehicleLicense) {
        return new VehicleLicenseDTOConverter().reverse().convert(vehicleLicense);
    }

    @Override
    protected Converter<VehicleLicenseDTO, VehicleLicense> getConverter() {
        return new VehicleLicenseDTOConverter();
    }
}
