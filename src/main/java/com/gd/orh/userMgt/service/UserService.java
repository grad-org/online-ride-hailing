package com.gd.orh.userMgt.service;

import com.gd.orh.entity.User;

public interface UserService {

    User findByUsername(String username);

    boolean isUserExisted(String username);

    User register(User user);

    User createPassenger(User user);

    User update(User user);
}
