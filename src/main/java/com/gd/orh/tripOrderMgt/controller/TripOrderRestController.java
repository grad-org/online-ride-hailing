package com.gd.orh.tripOrderMgt.controller;

import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.entity.Driver;
import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.TripOrder;
import com.gd.orh.tripOrderMgt.service.TripOrderService;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tripOrder")
public class TripOrderRestController {

    @Autowired
    private TripOrderService tripOrderService;

    @GetMapping("/search/findAllByPassenger/{passengerId}")
    public ResponseEntity<?> findAllByPassenger(
            @PathVariable("passengerId") Long passengerId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {

        Passenger passenger = new Passenger();
        passenger.setId(passengerId);
        passenger.setPage(page);
        passenger.setRows(rows);

        List<TripOrder> tripOrders = tripOrderService.findAllByPassenger(passenger);
        List<TripOrderDTO> tripOrderDTOs = Lists.newArrayList();

        tripOrders.forEach(each -> tripOrderDTOs.add(new TripOrderDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        TripOrder tripOrder = tripOrderService.findById(id);
        TripOrderDTO tripOrderDTO = new TripOrderDTO().convertFor(tripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTO));
    }

    @GetMapping("/search/findAllByPassenger/{driverId}")
    public ResponseEntity<?> findAllByDriver(
            @PathVariable("driverId") Long driverId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setPage(page);
        driver.setRows(rows);

        List<TripOrder> tripOrders = tripOrderService.findAllByDriver(driver);
        List<TripOrderDTO> tripOrderDTOs = Lists.newArrayList();

        tripOrders.forEach(each -> tripOrderDTOs.add(new TripOrderDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTOs));
    }
}
