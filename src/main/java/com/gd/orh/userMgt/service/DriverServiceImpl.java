package com.gd.orh.userMgt.service;

import com.gd.orh.entity.*;
import com.gd.orh.mapper.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private DrivingLicenseMapper drivingLicenseMapper;

    @Autowired
    private VehicleLicenseMapper vehicleLicenseMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    @Transactional(readOnly = true)
    public Driver findById(Long id) {
            return driverMapper.findById(id);
    }

    public Driver save(Driver driver) {
        driver.setDriverStatus(DriverStatus.PENDING_REVIEW);
        driverMapper.insertDriver(driver);

        DrivingLicense drivingLicense = driver.getDrivingLicense();
        drivingLicense.setDriver(driver);
        drivingLicenseMapper.insertDrivingLicense(drivingLicense);

        VehicleLicense vehicleLicense = driver.getVehicleLicense();
        vehicleLicense.setDriver(driver);
        vehicleLicenseMapper.insertVehicleLicense(vehicleLicense);

        Car car = vehicleLicense.getCar();
        car.setVehicleLicense(vehicleLicense);
        carMapper.insertCar(car);

        return driver;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Driver> findPendingReviewDriver(Driver driver) {
        driver.setDriverStatus(DriverStatus.PENDING_REVIEW);
        return findAllDriverByDriverStatus(driver);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Driver> findAllDriverByDriverStatus(Driver driver) {
        if (driver.getPage() != null && driver.getRows() != null) {
            PageHelper.startPage(driver.getPage(), driver.getRows());
        }
        return driverMapper.findAllByDriverStatus(driver);
    }

    @Override
    public Driver reviewDriver(Driver driver) {
        driverMapper.updateDriverStatus(driver);

        // If approved, grant driver authority.
        if (DriverStatus.APPROVED == driver.getDriverStatus()) {
            Driver updatedDriver = driverMapper.findById(driver.getId());
            User user = updatedDriver.getUser();
            user.setAuthorities(Arrays.asList(new Authority(AuthorityName.ROLE_DRIVER)));
            authorityMapper.insertUserAuthority(user);
        }

        return driverMapper.findById(driver.getId());
    }

    @Override
    public Driver updateVehicleLicense(Driver driver) {
        driver.setDriverStatus(DriverStatus.PENDING_REVIEW);
        driverMapper.updateDriverStatus(driver);

        Driver persistedDriver = driverMapper.findById(driver.getId());
        Long vehicleLicenseId = persistedDriver.getVehicleLicense().getId();
        Long carId = persistedDriver.getVehicleLicense().getCar().getId();

        VehicleLicense vehicleLicense = driver.getVehicleLicense();
        vehicleLicense.setId(vehicleLicenseId);
        vehicleLicenseMapper.updateByPrimaryKey(vehicleLicense);

        Car car = vehicleLicense.getCar();
        car.setId(carId);
        carMapper.updateByPrimaryKey(car);

        authorityMapper.deleteUserAuthority(persistedDriver.getUser().getId(), AuthorityName.ROLE_DRIVER.getId());

        return driverMapper.findById(driver.getId());
    }
}
