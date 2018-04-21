package com.gd.orh.hailingService.controller;

import com.gd.orh.entity.FareRule;
import com.gd.orh.hailingService.service.FareRuleService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fareRule")
public class FareRuleRestController {

    @Autowired
    private FareRuleService fareRuleService;

    @GetMapping("/search/findRecentFareRule")
    public ResponseEntity<?> findPublishedTrip() {

        FareRule fareRule = fareRuleService.findRecentFareRule();

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(fareRule));
    }
}
