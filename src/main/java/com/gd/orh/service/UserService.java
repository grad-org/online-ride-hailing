package com.gd.orh.service;

import com.gd.orh.entity.User;
import com.gd.orh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
}
