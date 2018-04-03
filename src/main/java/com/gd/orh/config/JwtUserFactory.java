package com.gd.orh.config;

import com.gd.orh.entity.Role;
import com.gd.orh.entity.User;
import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class JwtUserFactory {
    public static final UserConverter converter = new UserConverter();

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return converter.doForward(user);
    }

    private static class UserConverter extends Converter<User, JwtUser> {

        @Override
        protected JwtUser doForward(User user) {
            JwtUser jwtUser = new JwtUser();
            BeanUtils.copyProperties(user, jwtUser);
            jwtUser.setAuthorities(mapToGrantedAuthorities(user.getRoles()));
            return jwtUser;
        }

        @Override
        protected User doBackward(JwtUser jwtUser) {
            return null;
        }
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        List<GrantedAuthority> authorities = Lists.newArrayList();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
