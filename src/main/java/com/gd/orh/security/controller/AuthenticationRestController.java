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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("The token could not be refresh!"));
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public ResponseEntity<?> verify(@RequestParam String username) {
        if (userRepository.findByUsername(username) != null) {
            return ResponseEntity.badRequest().body(RestResultFactory.getFailResult("User is existed!"));
        } else {
            return ResponseEntity.ok().body(RestResultFactory.getSuccessResult());
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
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
        return ResponseEntity.ok(userRepository.save(newUser));
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
