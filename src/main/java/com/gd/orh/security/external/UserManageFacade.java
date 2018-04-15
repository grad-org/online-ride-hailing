package com.gd.orh.security.external;

import com.gd.orh.entity.User;

public interface UserManageFacade {

    User findByUsername(String username);

    boolean isUserExisted(String username);

    User registerPassenger(User user);
}
