package com.gd.orh.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
    private String token;
}
