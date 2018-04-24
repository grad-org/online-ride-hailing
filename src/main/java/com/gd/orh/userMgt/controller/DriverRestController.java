package com.gd.orh.userMgt.controller;

import com.gd.orh.dto.DriverDTO;
import com.gd.orh.dto.DrivingLicenseDTO;
import com.gd.orh.dto.VehicleLicenseDTO;
import com.gd.orh.entity.Driver;
import com.gd.orh.userMgt.service.DriverService;
import com.gd.orh.utils.FileUploadUtil;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/driver")
public class DriverRestController {

    @Autowired
    private DriverService driverService;

    // 认证车主
    @PostMapping("/certifyDriver")
    public ResponseEntity<?> authenticateDriver(@Valid DriverDTO driverDTO, BindingResult result) {

        // 验证逻辑
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "All properties could not empty!"
                    ));
        }

        // 保存驾驶证照片
        MultipartFile drivingLicenseImage = Optional
                .ofNullable(driverDTO)
                .map(DriverDTO::getDrivingLicenseDTO)
                .map(DrivingLicenseDTO::getDrivingLicenseImage)
                .orElse(null);

        if (drivingLicenseImage != null && !drivingLicenseImage.isEmpty()) {

            boolean isSucceed =
                    FileUploadUtil.upload(
                            drivingLicenseImage,
                            "static/images/drivingLicense/",
                            driverDTO.getUserId() + ".jpg"
                    );

            if (!isSucceed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Driving License Image saving failed!"));
            }
        }

        // 保存行驶证照片
        MultipartFile vehicleLicenseImage = Optional
                .ofNullable(driverDTO)
                .map(DriverDTO::getVehicleLicenseDTO)
                .map(VehicleLicenseDTO::getVehicleLicenseImage)
                .orElse(null);

        if (vehicleLicenseImage != null && !vehicleLicenseImage.isEmpty()) {

            boolean isSucceed =
                    FileUploadUtil.upload(
                            vehicleLicenseImage,
                            "static/images/vehicleLicense/",
                            driverDTO.getUserId() + ".jpg"
                    );

            if (!isSucceed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Vehicle License Image saving failed!"));
            }
        }

        Driver driver = driverDTO.convertTo();

        driver = driverService.save(driver);

        DriverDTO authenticatedDriverDTO = new DriverDTO().convertFor(driver);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(authenticatedDriverDTO));
    }
}
