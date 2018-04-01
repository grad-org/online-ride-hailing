package com.gd.orh.controller;

import com.gd.orh.entity.CarLocation;
import com.gd.orh.entity.Location;
import com.gd.orh.entity.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class LocationController {

    @MessageMapping("/carBroadcast")
    @SendTo("/topic/carBroadcast")
    public CarLocation broadcast(Location location, Principal principal) {
        CarLocation carLocation = new CarLocation();

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        User user = (User) token.getPrincipal();

        carLocation.setCarId(user.getNickname());
        carLocation.setLocation(location);

        return carLocation;
    }
}
