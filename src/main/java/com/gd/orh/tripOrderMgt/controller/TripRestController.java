package com.gd.orh.tripOrderMgt.controller;

import com.gd.orh.dto.TripDTO;
import com.gd.orh.entity.ListeningOrderCondition;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.Trip;
import com.gd.orh.tripOrderMgt.service.TripService;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripRestController {

    @Autowired
    private TripService tripService;

    @GetMapping("/search/findPublishedTripByCondition")
    public ResponseEntity<?> findPublishedTrip(ListeningOrderCondition condition) {
        List<Trip> trips = tripService.findPublishedTripsByListeningOrderCondition(condition);
        List<TripDTO> tripDTOs = Lists.newArrayList();

        trips.forEach(each -> tripDTOs.add(new TripDTO().convertFor(each)));
        
        return ResponseEntity
                .ok(RestResultFactory.getSuccessResult().setData(tripDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Trip trip = tripService.findById(id);
        if (trip == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "Not found trip with id: " + id + "!",
                        null
                    ));
        }

        TripDTO tripDTO = new TripDTO().convertFor(trip);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(tripDTO));
    }
}
