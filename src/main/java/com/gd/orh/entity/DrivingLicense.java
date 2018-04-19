package com.gd.orh.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class DrivingLicense extends BaseEntity {

    private MultipartFile drivingLicenseImage; // 驾驶证照片

    private String driverName; // 司机姓名

    private String identification; // 身份证号

    private Date issueDate; // 初次领取驾驶证日期

    private Driver driver;
}
