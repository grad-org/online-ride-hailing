package com.gd.orh.mapper;

import com.gd.orh.entity.User;

public interface AuthorityMapper {
    void insertUserAuthority(User user);

    void deleteUserAuthority(Long userId, Long authorityId);
}