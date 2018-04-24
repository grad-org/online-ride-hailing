package com.gd.orh.mapper;

import com.gd.orh.entity.DrivingLicense;
import com.gd.orh.utils.MyMapper;

public interface DrivingLicenseMapper extends MyMapper<DrivingLicense> {
    void insertDrivingLicense(DrivingLicense drivingLicense);
}