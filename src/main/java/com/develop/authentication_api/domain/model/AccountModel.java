package com.develop.authentication_api.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AccountModel {

    @NotNull(message = "USERNAME COULD NOT BE NULL")
    @Size(min = 5, message = "USERNAME SHOULD CONTAINS AT LEAST 5 CHARACTERS")
    private String username;

    @NotNull(message = "PASSWORD COULD NOT BE NULL")
    @Size(min = 5, message = "PASSWORD SHOULD CONTAINS AT LEAST 5 CHARACTERS")
    private String password;

    private List<Long> roles;
}
