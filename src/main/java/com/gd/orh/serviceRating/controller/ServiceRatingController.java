package com.gd.orh.serviceRating.controller;

import com.gd.orh.dto.ServiceRatingDTO;
import com.gd.orh.entity.ServiceRating;
import com.gd.orh.serviceRating.service.ServiceRatingService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/serviceRating")
public class ServiceRatingController {

    @Autowired
    private ServiceRatingService serviceRatingService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        ServiceRating serviceRating = serviceRatingService.findById(id);

        ServiceRatingDTO serviceRatingDTO = new ServiceRatingDTO().convertFor(serviceRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(serviceRatingDTO));
    }

    // 评价车主
    @PostMapping("/rateDriver")
    public ResponseEntity<?> rateDriver(@RequestBody ServiceRatingDTO serviceRatingDTO) {
        ServiceRating serviceRating = serviceRatingDTO.convertTo();

        ServiceRating ratedServiceRating = serviceRatingService.rateDriver(serviceRating);

        ServiceRatingDTO ratedServiceRatingDTO = new ServiceRatingDTO().convertFor(ratedServiceRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(ratedServiceRatingDTO));
    }

    // 评价乘客
    @PostMapping("/ratePassenger")
    public ResponseEntity<?> ratePassenger(@RequestBody ServiceRatingDTO serviceRatingDTO) {
        ServiceRating serviceRating = serviceRatingDTO.convertTo();

        ServiceRating ratedServiceRating = serviceRatingService.rateDriver(serviceRating);

        ServiceRatingDTO ratedServiceRatingDTO = new ServiceRatingDTO().convertFor(ratedServiceRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(ratedServiceRatingDTO));
    }
}
