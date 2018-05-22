package com.gd.orh.userMgt.service;

import com.gd.orh.entity.Authority;
import com.gd.orh.entity.AuthorityName;
import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.User;
import com.gd.orh.mapper.AuthorityMapper;
import com.gd.orh.mapper.PassengerMapper;
import com.gd.orh.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (!StringUtils.isEmpty(username)) {
            return userMapper.findByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserExisted(String username) {
        return this.findByUsername(username) != null;
    }

    @Override
    public User register(User user) {
        // Auditing basic info on new user.
        user.setEnabled(true);
        user.setLastPasswordResetDate(new Date());

        // Encrypt the password.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));

        // Save the user and return it.
        userMapper.insertUser(user);

        return user;
    }

    @Override
    public User createPassenger(User user) {
        // Create passenger information.
        Passenger newPassenger = new Passenger();
        newPassenger.setUser(user);

        passengerMapper.insertPassenger(newPassenger);

        // Grant passenger authority.
        user.setAuthorities(Arrays.asList(new Authority(AuthorityName.ROLE_PASSENGER)));
        authorityMapper.insertUserAuthority(user);

        return this.findById(user.getId());
    }

    @Override
    public User update(User user) {
        User persistedUser = userMapper.findById(user.getId());

        persistedUser.setNickname(user.getNickname());
        persistedUser.setGender(user.getGender());
        persistedUser.setAge(user.getAge());

        // update the user and return it.
        userMapper.updateUser(persistedUser);

        return persistedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(User user) {
        if (user.getPage() != null && user.getRows() != null) {
            PageHelper.startPage(user.getPage(), user.getRows());
        }
        return userMapper.findAll();
    }
}
