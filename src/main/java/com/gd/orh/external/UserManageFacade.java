package com.gd.orh.external;

import com.gd.orh.entity.User;

import java.security.Principal;

public interface UserManageFacade {

    User findUserByUsername(String username);

    boolean isUserExisted(String username);

    User registerPassenger(User user);

    User findUserByPrincipal(Principal principal);
}
