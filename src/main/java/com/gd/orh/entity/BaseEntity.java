package com.gd.orh.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseEntity {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer page = 1;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer rows = 10;
}
