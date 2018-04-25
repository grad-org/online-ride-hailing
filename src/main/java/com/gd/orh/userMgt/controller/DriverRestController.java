package com.gd.orh.userMgt.controller;

import com.gd.orh.dto.DriverDTO;
import com.gd.orh.dto.DrivingLicenseDTO;
import com.gd.orh.dto.VehicleLicenseDTO;
import com.gd.orh.entity.Driver;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.userMgt.service.DriverService;
import com.gd.orh.utils.FileUploadUtil;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
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

            boolean isFail =
                    !FileUploadUtil.upload(
                            drivingLicenseImage,
                            "static/images/drivingLicense/",
                            driverDTO.getDriverId() + ".jpg"
                    );

            if (isFail) {
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

            boolean isFail =
                    !FileUploadUtil.upload(
                            vehicleLicenseImage,
                            "static/images/vehicleLicense/",
                            driverDTO.getDriverId() + ".jpg"
                    );

            if (isFail) {
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

    @GetMapping("/search/findPendingReviewDriver")
    public ResponseEntity<?> findAllPendingReviewDriver(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {
        Driver driver = new Driver();

        driver.setPage(page);
        driver.setRows(rows);

        List<Driver> drivers = driverService.findPendingReviewDriver(driver);
        List<DriverDTO> driverDTOs = Lists.newArrayList();

        drivers.forEach(each -> driverDTOs.add(new DriverDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(driverDTOs));
    }

    // 审核车主资料
    @PostMapping("/reviewDriver")
    public ResponseEntity<?> reviewDriver(DriverDTO driverDTO) {

        Driver driver = driverDTO.convertTo();

        driver = driverService.reviewDriver(driver);

        DriverDTO reviewedDriverDTO = new DriverDTO().convertFor(driver);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(reviewedDriverDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Driver driver = driverService.findById(id);
        if (driver == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "Not found driver with id: " + id + "!",
                            null
                    ));
        }

        DriverDTO driverDTO = new DriverDTO().convertFor(driver);

        // Return driver.
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(driverDTO));
    }
}
