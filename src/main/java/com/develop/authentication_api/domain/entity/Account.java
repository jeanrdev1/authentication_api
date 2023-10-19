package com.develop.authentication_api.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Column(updatable = false)
    private UUID uuid;

    @Column(updatable = false)
    private LocalDateTime created;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled = true;

    public void addRole(Role role) {
        roles.add(role);
    }
}
