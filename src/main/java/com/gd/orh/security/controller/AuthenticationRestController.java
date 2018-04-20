package com.gd.orh.security.controller;

import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.User;
import com.gd.orh.security.JwtTokenUtil;
import com.gd.orh.security.JwtUser;
import com.gd.orh.external.UserManageFacade;
import com.gd.orh.utils.RestResultFactory;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
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
    private UserManageFacade userManageFacade;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity
                .ok(RestResultFactory.getSuccessResult().setData(new JwtAuthenticationResponse(token)));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserWithAuthenticationToken(Principal principal) {
        User user = userManageFacade.findUserByUsername(principal.getName());

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
            return ResponseEntity
                    .ok(RestResultFactory.getSuccessResult().setData(
                        new JwtAuthenticationResponse(refreshedToken)
                    ));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("The token could not be refresh!"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String username) {
        if (!userManageFacade.isUserExisted(username)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                        ResultCode.NOT_FOUND,
                        "The username is not existed!",
                        null
                    ));
        } else {
            return ResponseEntity.ok(RestResultFactory.getSuccessResult());
        }
    }

    @PostMapping("/registerPassenger")
    public ResponseEntity<?> registerPassenger(@RequestBody @Valid User newUser, BindingResult result) {
        // If username or password is empty, register fail.
        if (result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                        "The username or password could not be empty!"
                    ));
        }

        // If passenger is existed, register fail,
        // else register the user with Passenger.
        User persistedUser = userManageFacade.findUserByUsername(newUser.getUsername());
        if (persistedUser != null && persistedUser.getPassenger().getId() != null) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("passenger is existed!"));
        } else {
            User user = userManageFacade.registerPassenger(newUser);
            return ResponseEntity.ok(RestResultFactory.getSuccessResult().setData(user));
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(RestResultFactory.getUnauthorizedResult(e.getMessage()));
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
