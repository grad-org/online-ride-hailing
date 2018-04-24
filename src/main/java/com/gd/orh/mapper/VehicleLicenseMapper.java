package com.gd.orh.mapper;

import com.gd.orh.entity.VehicleLicense;
import com.gd.orh.utils.MyMapper;

public interface VehicleLicenseMapper extends MyMapper<VehicleLicense> {
    void insertVehicleLicense(VehicleLicense vehicleLicense);
}