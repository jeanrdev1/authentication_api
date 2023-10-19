package com.develop.authentication_api.controller;

import com.develop.authentication_api.domain.entity.Account;
import com.develop.authentication_api.domain.model.AccountModel;
import com.develop.authentication_api.domain.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(AccountController.PATH)
public class AccountController {

    public static final String PATH = "account";

    private final AccountService service;

    @GetMapping
    public Account getAuthenticated() {
        return service.getAuthenticated();
    }

    @PostMapping
    public Account register(@RequestBody @Valid AccountModel accountModel) {
        return service.register(accountModel);
    }

    @PostMapping("add-role")
    public void addRole(@RequestParam String username, @RequestParam Long id) {
        service.addRole(username, id);
    }
}
