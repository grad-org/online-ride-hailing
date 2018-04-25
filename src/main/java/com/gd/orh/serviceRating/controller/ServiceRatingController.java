package com.gd.orh.serviceRating.controller;

import com.gd.orh.dto.DriverRatingDTO;
import com.gd.orh.dto.PassengerRatingDTO;
import com.gd.orh.entity.DriverRating;
import com.gd.orh.entity.PassengerRating;
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

    // 评价车主
    @PostMapping("/rateDriver")
    public ResponseEntity<?> rateDriver(@RequestBody DriverRatingDTO driverRatingDTO) {
        DriverRating driverRating = driverRatingDTO.convertTo();

        driverRating = serviceRatingService.rateDriver(driverRating);

        DriverRatingDTO ratedDriverRatingDTO = new DriverRatingDTO().convertFor(driverRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(ratedDriverRatingDTO));
    }

    // 评价乘客
    @PostMapping("/ratePassenger")
    public ResponseEntity<?> ratePassenger(@RequestBody PassengerRatingDTO passengerRatingDTO) {
        PassengerRating passengerRating = passengerRatingDTO.convertTo();

        passengerRating = serviceRatingService.ratePassenger(passengerRating);

        PassengerRatingDTO ratedPassengerRatingDTO = new PassengerRatingDTO().convertFor(passengerRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(ratedPassengerRatingDTO));
    }

    // 获取车主评价
    @GetMapping("/driverRating/{id}")
    public ResponseEntity<?> getDriverRating(@PathVariable Long id) {
        DriverRating driverRating = serviceRatingService.findDriverRatingById(id);

        DriverRatingDTO driverRatingDTO = new DriverRatingDTO().convertFor(driverRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(driverRatingDTO));
    }

    // 获取乘客评价
    @GetMapping("/passengerRating/{id}")
    public ResponseEntity<?> getPassengerRating(@PathVariable Long id) {
        PassengerRating passengerRating = serviceRatingService.findPassengerRatingById(id);

        PassengerRatingDTO passengerRatingDTO = new PassengerRatingDTO().convertFor(passengerRating);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(passengerRatingDTO));
    }
}
