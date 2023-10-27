package com.develop.authentication_api.domain.service;

import com.develop.authentication_api.domain.repository.AccountRepository;
import com.develop.authentication_api.domain.entity.Account;
import com.develop.authentication_api.domain.entity.Role;
import com.develop.authentication_api.domain.model.AccountModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public Account getAuthenticated() {
        Optional<Account> account = repository.findAccountByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (account.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("Username {0} not found!", SecurityContextHolder.getContext().getAuthentication().getName()));
        }
        return account.get();
    }

    public Account register(AccountModel accountModel) {
        verifyUsername(accountModel.getUsername());
        Account account = new Account();
        account.setUsername(accountModel.getUsername());
        account.setPassword(passwordEncoder.encode(accountModel.getPassword()));
        account.setRoles(roleService.addRoleList(accountModel.getRoles()));
        account.setCreated(LocalDateTime.now());
        account.setUuid(UUID.randomUUID());
        return repository.save(account);
    }

    public void addRole(String username, Long id) {
        Optional<Account> result = repository.findAccountByUsername(username);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        Account account = result.get();
        Role role = roleService.getRole(id);
        if (account.getRoles().stream().anyMatch(userRole -> userRole.equals(role))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Permission already exists");
        }
        account.addRole(role);
        repository.save(account);
    }

    private void verifyUsername(String username) {
        if (repository.findAccountByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MessageFormat.format("Username {0} already registered", username));
        }
    }
}
