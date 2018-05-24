package com.gd.orh.incomeExpMgt.controller;

import com.gd.orh.dto.DriverBalanceDTO;
import com.gd.orh.entity.DriverBalance;
import com.gd.orh.incomeExpMgt.service.DriverBalanceService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driverBalance")
public class DriverBalanceController {

    @Autowired
    private DriverBalanceService driverBalanceService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        DriverBalance driverBalance = driverBalanceService.findById(id);

        DriverBalanceDTO driverBalanceDTO = new DriverBalanceDTO().convertFor(driverBalance);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(driverBalanceDTO));
    }

    @PostMapping("/bindAlipayAccount")
    public ResponseEntity<?> bindAlipayAccount(@RequestBody DriverBalanceDTO driverBalanceDTO) {
        DriverBalance driverBalance = driverBalanceDTO.convertTo();

        DriverBalance bindDriverBalance = driverBalanceService.bindAlipayAccount(driverBalance);

        DriverBalanceDTO bindDriverBalanceDTO = new DriverBalanceDTO().convertFor(bindDriverBalance);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(bindDriverBalanceDTO));
    }
}
