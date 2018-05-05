package com.gd.orh.tripOrderMgt.controller;

import com.gd.orh.dto.TripOrderDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        TripOrder tripOrder = tripOrderService.findById(id);
        TripOrderDTO tripOrderDTO = new TripOrderDTO().convertFor(tripOrder);

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTO));
    }

    @GetMapping("/search/findAllByPassenger")
    public ResponseEntity<?> findAllByPassenger(
            @RequestParam("passengerId") Long passengerId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {

        TripOrderDTO tripOrderDTO = new TripOrderDTO();
        tripOrderDTO.setPassengerId(passengerId);
        TripOrder tripOrder = tripOrderDTO.convertTo();
        tripOrder.setPage(page);
        tripOrder.setRows(rows);

        List<TripOrder> tripOrders = tripOrderService.findAllByPassenger(tripOrder);
        List<TripOrderDTO> tripOrderDTOs = Lists.newArrayList();

        tripOrders.forEach(each -> tripOrderDTOs.add(new TripOrderDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTOs));
    }

    @GetMapping("/search/findAllByDriver")
    public ResponseEntity<?> findAllByDriver(
            @RequestParam("driverId") Long driverId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer rows) {

        TripOrderDTO tripOrderDTO = new TripOrderDTO();
        tripOrderDTO.setDriverId(driverId);
        TripOrder tripOrder = tripOrderDTO.convertTo();
        tripOrder.setPage(page);
        tripOrder.setRows(rows);

        List<TripOrder> tripOrders = tripOrderService.findAllByDriver(tripOrder);
        List<TripOrderDTO> tripOrderDTOs = Lists.newArrayList();

        tripOrders.forEach(each -> tripOrderDTOs.add(new TripOrderDTO().convertFor(each)));

        return ResponseEntity.ok(RestResultFactory.getSuccessResult(tripOrderDTOs));
    }
}
