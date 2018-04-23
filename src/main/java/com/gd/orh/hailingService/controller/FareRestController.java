package com.gd.orh.hailingService.controller;

import com.gd.orh.dto.FareDTO;
import com.gd.orh.entity.Fare;
import com.gd.orh.hailingService.service.FareService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fare")
public class FareRestController {

    @Autowired
    private FareService fareService;

    // 预估车费
    @GetMapping("/predictFare")
    public ResponseEntity<?> predictFare(@Valid FareDTO fareDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "The giving mileage or time could not be empty and must be greater than 0!"
                    ));
        }

        Fare fare = fareDTO.convertToFare();

        Fare predictedFare = fareService.predictFare(fare);

        FareDTO predictedFareDTO = new FareDTO().convertFor(predictedFare);

        // 返回预估车费
        return ResponseEntity.ok(RestResultFactory.getSuccessResult(predictedFareDTO));
    }
}
