package com.gd.orh.fareRuleMgt.controller;

import com.gd.orh.dto.FareRuleDTO;
import com.gd.orh.entity.FareRule;
import com.gd.orh.fareRuleMgt.service.FareRuleService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fareRule")
public class FareRuleRestController {

    @Autowired
    private FareRuleService fareRuleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        FareRule fareRule = fareRuleService.findById(id);

        FareRuleDTO fareRuleDTO = new FareRuleDTO().convertFor(fareRule);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(fareRuleDTO));
    }

    @PostMapping
    public ResponseEntity<?> setup(@RequestBody @Valid FareRuleDTO fareRuleDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "Please check your fare rule's property format!"
                    ));
        }

        FareRule fareRule = fareRuleDTO.convertTo();

        fareRule = fareRuleService.save(fareRule);

        FareRuleDTO savedFareRuleDTO = new FareRuleDTO().convertFor(fareRule);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(savedFareRuleDTO));
    }
}
