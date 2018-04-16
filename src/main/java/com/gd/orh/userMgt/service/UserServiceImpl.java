package com.gd.orh.userMgt.service;

import com.gd.orh.entity.AuthorityName;
import com.gd.orh.entity.Passenger;
import com.gd.orh.entity.User;
import com.gd.orh.mapper.UserMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        Weekend<User> weekend = Weekend.of(User.class);
        WeekendCriteria<User, Object> criteria = weekend.weekendCriteria();
        if (StringUtils.isNotEmpty(username)) {
            criteria.andEqualTo("username", username);
            return userMapper.selectOneByExample(weekend);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserExisted(String username) {
        return findByUsername(username) != null;
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
        Long id = Long.valueOf(userMapper.insert(user));

        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User createPassenger(User user) {
        // Create passenger information.
        Passenger newPassenger = new Passenger();
        user.setPassenger(newPassenger);
        newPassenger.setUser(user);

        // Grant passenger authority.
        user.setAuthorities(Lists.newArrayList(
                authorityRepository.findByName(AuthorityName.ROLE_PASSENGER)));

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User persistedUser = userMapper.selectByPrimaryKey(user.getId());

        persistedUser.setNickname(user.getNickname());
        persistedUser.setGender(user.getGender());
        persistedUser.setAge(user.getAge());

        // update the user and return it.
        userMapper.updateByPrimaryKey(persistedUser);

        return persistedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userMapper.selectAll();
    }
}
