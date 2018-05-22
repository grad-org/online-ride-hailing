package com.gd.orh.userMgt.controller;

import com.gd.orh.dto.DriverDTO;
import com.gd.orh.dto.DrivingLicenseDTO;
import com.gd.orh.dto.VehicleLicenseDTO;
import com.gd.orh.entity.Driver;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.userMgt.service.DriverService;
import com.gd.orh.utils.ImageUploadUtil;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/driver")
public class DriverRestController {

    @Autowired
    private DriverService driverService;

    // 认证车主
    @PostMapping("/certifyDriver")
    public ResponseEntity<?> certifyDriver(@Valid @RequestBody DriverDTO driverDTO, BindingResult result) {

        // 验证逻辑
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "All properties could not empty!"
                    ));
        }

        // 保存驾驶证照片
        String drivingLicenseImage = Optional
                .ofNullable(driverDTO)
                .map(DriverDTO::getDrivingLicense)
                .map(DrivingLicenseDTO::getDrivingLicenseImage)
                .orElse(null);

        if (ImageUploadUtil.isImageNotEmpty(drivingLicenseImage)) {

            String imageContent = ImageUploadUtil.getImageContent(drivingLicenseImage);

            if (imageContent == null) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("The upload file is not a image!"));
            }

            InputStream in = ImageUploadUtil.getInputStreamFromImageContent(imageContent);

            boolean isUploadFailed
                        = !ImageUploadUtil.uploadImageToRootPath(
                            "images/drivingLicense/",
                            driverDTO.getDriverId() + ".jpg",
                            in,
                            "jpg"
                        );

            if (isUploadFailed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Upload Driving License Image failed!"));
            }
        }

        // 保存行驶证照片
        String vehicleLicenseImage = Optional
                .ofNullable(driverDTO)
                .map(DriverDTO::getVehicleLicense)
                .map(VehicleLicenseDTO::getVehicleLicenseImage)
                .orElse(null);

        if (ImageUploadUtil.isImageNotEmpty(vehicleLicenseImage)) {

            String imageContent = ImageUploadUtil.getImageContent(vehicleLicenseImage);

            if (imageContent == null) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("The upload file is not a image!"));
            }

            InputStream in = ImageUploadUtil.getInputStreamFromImageContent(imageContent);

            boolean isUploadFailed
                        = !ImageUploadUtil.uploadImageToRootPath(
                            "images/vehicleLicense/",
                            driverDTO.getDriverId() + ".jpg",
                            in,
                            "jpg"
                        );

            if (isUploadFailed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Upload Vehicle License Image failed!"));
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
    public ResponseEntity<?> reviewDriver(@RequestBody DriverDTO driverDTO) {

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

    // 修改行驶证资料
    @PostMapping("/updateVehicleLicense/{driverId}")
    public ResponseEntity<?> updateVehicleLicense(
            @PathVariable("driverId") Long driverId,
            @Valid @RequestBody DriverDTO driverDTO,
            BindingResult result) {

        // 验证逻辑
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "All properties could not empty!"
                    ));
        }

        // 保存行驶证照片
        String vehicleLicenseImage = Optional
                .ofNullable(driverDTO)
                .map(DriverDTO::getVehicleLicense)
                .map(VehicleLicenseDTO::getVehicleLicenseImage)
                .orElse(null);

        if (ImageUploadUtil.isImageNotEmpty(vehicleLicenseImage)) {

            String imageContent = ImageUploadUtil.getImageContent(vehicleLicenseImage);

            if (imageContent == null) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("The upload file is not a image!"));
            }

            InputStream in = ImageUploadUtil.getInputStreamFromImageContent(imageContent);

            boolean isUploadFailed
                        = !ImageUploadUtil.uploadImageToRootPath(
                            "images/vehicleLicense/",
                            driverId + ".jpg",
                            in,
                            "jpg"
                        );

            if (isUploadFailed) {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Upload Vehicle License Image failed!"));
            }
        }

        driverDTO.setDriverId(driverId);
        Driver driver = driverDTO.convertTo();

        driver = driverService.updateVehicleLicense(driver);

        DriverDTO authenticatedDriverDTO = new DriverDTO().convertFor(driver);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(authenticatedDriverDTO));
    }
}
