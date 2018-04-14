package com.gd.orh.controller;

import com.gd.orh.entity.User;
import com.gd.orh.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
//    @Value("${jwt.header}")
//    private String tokenHeader;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    @Qualifier("jwtUserDetailsService")
//    private UserDetailsService userDetailsService;

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public List<User> getUsers() {
//        return repository.findAll();
//    }
//
//    @PostAuthorize("returnObject.username == principal.username or hasRole('ADMIN')")
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable("id") Long id) {
//        return repository.getOne(id);
//    }
//
//    @PreAuthorize("hasRole('PASSENGER') && #username == principal.username or hasRole('ADMIN')")
//    @GetMapping("/search/findByUsername/{username}")
//    public User getUserByUsername(@PathVariable("username") String username) {
//        return repository.findByUsername(username);
//    }
//
//    @GetMapping("/auth")
//    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
//        String token = request.getHeader(tokenHeader).substring(7);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
//        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
//        return user;
//    }

    @Autowired
    private UserRepository repository;

    @PutMapping("")
    public ResponseEntity<?> updateUser(User updatedUser) {
        return null;
    }
}
