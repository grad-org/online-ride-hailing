package com.gd.orh.entity;

import lombok.Getter;

@Getter
public enum AuthorityName {
    ROLE_PASSENGER(1l), ROLE_DRIVER(2l), ROLE_ADMIN(3l);

    private final Long id;

    AuthorityName(Long id) {
        this.id = id;
    }
}
