package com.gd.orh.external;

import com.gd.orh.entity.User;
import com.gd.orh.security.JwtUser;
import com.gd.orh.userMgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Transactional
@Component
public class UserManageFacadeImpl implements UserManageFacade {
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public User findUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isUserExisted(String username) {
        return userService.isUserExisted(username);
    }

    @Override
    public User registerPassenger(User user) {
        User registeredUser = null;

        // If username is not existed, register the user
        if (!userService.isUserExisted(user.getUsername())) {
            registeredUser = userService.register(user);
        }
        // else only create Passenger
        User registeredUserWithPassenger = userService.createPassenger(registeredUser);

        return registeredUserWithPassenger;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByPrincipal(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        JwtUser user = (JwtUser) token.getPrincipal();
        return userService.findById(user.getId());
    }
}
