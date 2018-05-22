package com.gd.orh.mapper;

import com.gd.orh.entity.Authority;
import com.gd.orh.entity.User;
import com.gd.orh.utils.MyMapper;

public interface AuthorityMapper extends MyMapper<Authority> {
    void insertUserAuthority(User user);

    void deleteUserAuthority(Long userId, Long authorityId);
}