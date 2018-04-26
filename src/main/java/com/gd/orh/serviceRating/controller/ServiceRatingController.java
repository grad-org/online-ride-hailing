package com.gd.orh.serviceRating.controller;

import com.gd.orh.dto.DriverComplaintDTO;
import com.gd.orh.dto.DriverRatingDTO;
import com.gd.orh.dto.PassengerComplaintDTO;
import com.gd.orh.dto.PassengerRatingDTO;
import com.gd.orh.entity.DriverComplaint;
import com.gd.orh.entity.DriverRating;
import com.gd.orh.entity.PassengerComplaint;
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

    // 投诉车主
    @PostMapping("/complainDriver")
    public ResponseEntity<?> complainDriver(@RequestBody DriverComplaintDTO driverComplaintDTO) {
        DriverComplaint driverComplaint = driverComplaintDTO.convertTo();

        driverComplaint = serviceRatingService.complainDriver(driverComplaint);

        DriverComplaintDTO complainedDriverComplaintDTO = new DriverComplaintDTO().convertFor(driverComplaint);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(complainedDriverComplaintDTO));
    }

    // 评价乘客
    @PostMapping("/complainPassenger")
    public ResponseEntity<?> complainPassenger(@RequestBody PassengerComplaintDTO passengerComplaintDTO) {
        PassengerComplaint passengerComplaint = passengerComplaintDTO.convertTo();

        passengerComplaint = serviceRatingService.complainPassenger(passengerComplaint);

        PassengerComplaintDTO complainedPassengerRatingDTO = new PassengerComplaintDTO().convertFor(passengerComplaint);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(complainedPassengerRatingDTO));
    }

    // 获取车主投诉
    @GetMapping("/driverComplaint/{id}")
    public ResponseEntity<?> getDriverComplaint(@PathVariable Long id) {
        DriverComplaint driverComplaint = serviceRatingService.findDriverComplaintById(id);

        DriverComplaintDTO driverComplaintDTO = new DriverComplaintDTO().convertFor(driverComplaint);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(driverComplaintDTO));
    }

    // 获取乘客投诉
    @GetMapping("/passengerComplaint/{id}")
    public ResponseEntity<?> getPassengerComplaint(@PathVariable Long id) {
        PassengerComplaint passengerComplaint = serviceRatingService.findPassengerComplaintById(id);

        PassengerComplaintDTO passengerComplaintDTO = new PassengerComplaintDTO().convertFor(passengerComplaint);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(passengerComplaintDTO));
    }
}
