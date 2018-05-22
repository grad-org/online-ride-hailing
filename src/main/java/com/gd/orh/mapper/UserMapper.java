package com.gd.orh.mapper;

import com.gd.orh.entity.User;

import java.util.List;

public interface UserMapper {
    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    void updateUser(User user);

    void insertUser(User user);
}