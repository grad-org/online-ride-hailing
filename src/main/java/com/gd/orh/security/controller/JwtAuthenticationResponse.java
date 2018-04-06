package com.gd.orh.security.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
    private final String token;
}
