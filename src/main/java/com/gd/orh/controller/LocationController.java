package com.gd.orh.controller;

import com.gd.orh.entity.CarLocation;
import com.gd.orh.entity.Location;
import com.gd.orh.entity.User;
import com.gd.orh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class LocationController {

    @Autowired
    private UserService userService;

    @MessageMapping("/carBroadcast")
    @SendTo("/topic/carBroadcast")
    public CarLocation broadcast(Location location, Principal principal) throws Exception {
        CarLocation carLocation = new CarLocation();

        String username = principal.getName();
        User user = userService.findByUsername(username);

        carLocation.setCarId(user.getNickname());
        carLocation.setLocation(location);

        return carLocation;
    }
}
