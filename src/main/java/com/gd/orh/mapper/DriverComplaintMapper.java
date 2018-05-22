package com.gd.orh.mapper;

import com.gd.orh.entity.DriverComplaint;

public interface DriverComplaintMapper {
    void insertDriverComplaint(DriverComplaint driverComplaint);

    DriverComplaint findById(Long id);
}