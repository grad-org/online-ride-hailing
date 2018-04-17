package com.gd.orh.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Authority extends BaseEntity {
    private AuthorityName name;

    public Authority(AuthorityName name) {
        setId(name.getId());
        this.name = name;
    }
}
