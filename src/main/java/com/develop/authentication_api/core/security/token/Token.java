package com.develop.authentication_api.core.security.token;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accessToken;

    private String refreshToken;
}
