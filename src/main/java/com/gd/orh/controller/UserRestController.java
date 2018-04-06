package com.gd.orh.controller;

import com.gd.orh.entity.User;
import com.gd.orh.security.JwtTokenUtil;
import com.gd.orh.security.JwtUser;
import com.gd.orh.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository repository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getUsers() {
        return repository.findAll();
    }

    @PostAuthorize("returnObject.username == principal.username or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return repository.getOne(id);
    }

    @PreAuthorize("hasRole('PASSENGER') && #username == principal.username or hasRole('ADMIN')")
    @GetMapping("/search/findByUsername/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return repository.findByUsername(username);
    }

    @GetMapping("/auth")
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }
}
