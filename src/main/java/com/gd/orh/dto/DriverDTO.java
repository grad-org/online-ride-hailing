package com.gd.orh.dto;

import com.gd.orh.entity.*;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Data
public class DriverDTO extends BaseDTO<DriverDTO, Driver> {

    private Long driverId;

    private DriverStatus driverStatus;

    private Long userId;

    private DrivingLicenseDTO drivingLicense;

    private VehicleLicenseDTO vehicleLicense;

    private CarDTO car;

    private static class DriverDTOConverter extends Converter<DriverDTO, Driver> {
        @Override
        protected Driver doForward(DriverDTO driverDTO) {
            Driver driver = new Driver();
            BeanUtils.copyProperties(driverDTO, driver);

            driver.setId(driverDTO.getDriverId());

            User user = new User();
            user.setId(driverDTO.getUserId());
            driver.setUser(user);

            DrivingLicense drivingLicense = Optional
                    .ofNullable(driverDTO)
                    .map(DriverDTO::getDrivingLicense)
                    .map(DrivingLicenseDTO::convertTo)
                    .orElse(null);
            driver.setDrivingLicense(drivingLicense);

            VehicleLicense vehicleLicense = Optional
                    .ofNullable(driverDTO)
                    .map(DriverDTO::getVehicleLicense)
                    .map(VehicleLicenseDTO::convertTo)
                    .orElse(null);
            driver.setVehicleLicense(vehicleLicense);

            Car car = Optional
                    .ofNullable(driverDTO)
                    .map(DriverDTO::getCar)
                    .map(CarDTO::convertTo)
                    .orElse(null);
            if (vehicleLicense != null) vehicleLicense.setCar(car);

            return driver;
        }

        @Override
        protected DriverDTO doBackward(Driver driver) {
            DriverDTO driverDTO = new DriverDTO();
            BeanUtils.copyProperties(driver, driverDTO);

            driverDTO.setDriverId(driver.getId());

            driverDTO.setUserId(Optional
                    .ofNullable(driver)
                    .map(Driver::getUser)
                    .map(User::getId)
                    .orElse(null));

            if (driver.getDrivingLicense() != null)
                driverDTO.setDrivingLicense(new DrivingLicenseDTO().convertFor(driver.getDrivingLicense()));

            if (driver.getVehicleLicense() != null) {
                driverDTO.setVehicleLicense(new VehicleLicenseDTO().convertFor(driver.getVehicleLicense()));

                if (driver.getVehicleLicense().getCar() != null) {
                    driverDTO.setCar(new CarDTO().convertFor(driver.getVehicleLicense().getCar()));
                }
            }

            return driverDTO;
        }
    }

    @Override
    protected Converter<DriverDTO, Driver> getConverter() {
        return new DriverDTOConverter();
    }
}
