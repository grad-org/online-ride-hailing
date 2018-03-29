package com.gd.orh.controller;

import com.gd.orh.domain.CarLocation;
import com.gd.orh.domain.Location;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Random;

@Controller
public class LocationController {

    @MessageMapping("/carBroadcast")
    @SendTo("/topic/carBroadcast")
    public CarLocation broadcast(Location location, Principal principal) throws Exception {
        CarLocation carLocation = new CarLocation();
        carLocation.setCarId(principal.getName());
        carLocation.setLocation(location);

        return carLocation;
    }
}
