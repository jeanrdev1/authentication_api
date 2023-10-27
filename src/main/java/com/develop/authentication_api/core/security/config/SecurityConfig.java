package com.develop.authentication_api.core.security.config;

import com.develop.authentication_api.core.security.token.TokenStore;
import com.develop.authentication_api.domain.service.CustomUserDetailsService;
import com.develop.authentication_api.core.security.filter.CustomAuthenticationFilter;
import com.develop.authentication_api.core.security.filter.CustomAuthorizationFilter;
import com.develop.authentication_api.core.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenStore tokenStore;

    private final JWTUtils jwtUtils;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager, tokenStore);
        authenticationFilter.setFilterProcessesUrl("/auth/login");
        http.csrf(AbstractHttpConfigurer::disable) // (csrf) -> csrf.disable()
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/unsafe/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/account", "/account/add-role", "/role").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomAuthorizationFilter(customUserDetailsService, tokenStore, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilter(authenticationFilter);
        return http.build();
    }
}
