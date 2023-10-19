package com.develop.authentication_api.domain.service;

import com.develop.authentication_api.domain.repository.AccountRepository;
import com.develop.authentication_api.domain.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = getAccount(username);
        return new User(account.getUsername(), account.getPassword(), account.getRoles());
    }

    private Account getAccount(String username) {
        Optional<Account> account = repository.findAccountByUsername(username);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException(MessageFormat.format("Username {0} not found!", username));
        }
        return account.get();
    }
}
