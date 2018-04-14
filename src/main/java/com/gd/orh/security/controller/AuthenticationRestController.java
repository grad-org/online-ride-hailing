package com.gd.orh.security.controller;

import com.gd.orh.entity.AuthorityName;
import com.gd.orh.entity.User;
import com.gd.orh.security.JwtTokenUtil;
import com.gd.orh.security.JwtUser;
import com.gd.orh.security.repository.AuthorityRepository;
import com.gd.orh.security.repository.UserRepository;
import com.gd.orh.utils.RestResultFactory;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(new JwtAuthenticationResponse(token)));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserWithAuthenticationToken(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());

        if (user != null) {
            return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(user));
        } else {
            throw new AuthenticationException("The token is invalid!");
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request, Principal principal) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(principal.getName());

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(new JwtAuthenticationResponse(refreshedToken)));
        } else {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("The token could not be refresh!"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String username) {
        if (userRepository.findByUsername(username) == null) {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("The username is not existed!"));
        } else {
            return ResponseEntity.ok().body(RestResultFactory.getSuccessResult());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid User newUser, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("The username or password could not be empty!"));
        }

        newUser.setEnabled(true);
        newUser.setLastPasswordResetDate(new Date());

        String username = newUser.getUsername();
        if (userRepository.findByUsername(username) != null) {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("User is existed!"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = newUser.getPassword();
        newUser.setPassword(encoder.encode(rawPassword));
        newUser.setAuthorities(Lists.newArrayList(authorityRepository.findByName(AuthorityName.ROLE_PASSENGER)));
        return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(userRepository.save(newUser)));
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResultFactory.getUnauthorizedResult(e.getMessage()));
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
