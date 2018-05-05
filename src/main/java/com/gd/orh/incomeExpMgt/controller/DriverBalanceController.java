package com.gd.orh.incomeExpMgt.controller;

import com.gd.orh.dto.DriverBalanceDTO;
import com.gd.orh.entity.DriverBalance;
import com.gd.orh.incomeExpMgt.service.DriverBalanceService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driverBalance")
public class DriverBalanceController {

    @Autowired
    private DriverBalanceService driverBalanceService;

    @GetMapping("/search/driver/{driverId}")
    public ResponseEntity<?> findByDriverId(@PathVariable("driverId") Long driverId) {
        DriverBalance driverBalance = driverBalanceService.findByDriverId(driverId);

        DriverBalanceDTO driverBalanceDTO = new DriverBalanceDTO().convertFor(driverBalance);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(driverBalanceDTO));
    }
}
