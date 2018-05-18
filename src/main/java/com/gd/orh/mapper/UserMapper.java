package com.gd.orh.mapper;

import com.gd.orh.entity.User;
import com.gd.orh.utils.MyMapper;

import java.util.List;

public interface UserMapper extends MyMapper<User> {
    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    void updateUser(User user);
}