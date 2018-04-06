package com.gd.orh.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NAME")
    private AuthorityName name;

//    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
//    private List<User> users;
}
