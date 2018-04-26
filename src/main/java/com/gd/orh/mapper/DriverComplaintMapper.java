package com.gd.orh.mapper;

import com.gd.orh.entity.DriverComplaint;
import com.gd.orh.utils.MyMapper;

public interface DriverComplaintMapper extends MyMapper<DriverComplaint> {
    void insertDriverComplaint(DriverComplaint driverComplaint);

    DriverComplaint findById(Long id);
}