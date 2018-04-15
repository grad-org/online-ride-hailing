package com.gd.orh.security.external;

import com.gd.orh.entity.User;
import com.gd.orh.userMgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class UserManageFacadeImpl implements UserManageFacade {
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isUserExisted(String username) {
        return userService.isUserExisted(username);
    }

    @Override
    public User registerPassenger(User user) {
        // If username is existed, return null.
        if (userService.isUserExisted(user.getUsername())) {
            return null;
        }

        // register the user
        User registeredUser = userService.register(user);

        // create Passenger
        User registeredUserWithPassenger = userService.createPassenger(registeredUser);

        return registeredUserWithPassenger;
    }
}
