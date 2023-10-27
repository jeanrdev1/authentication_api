package com.develop.authentication_api.core.security.filter;

import com.develop.authentication_api.core.security.token.TokenModel;
import com.develop.authentication_api.core.security.token.TokenStore;
import com.develop.authentication_api.core.utils.JWTUtils;
import com.develop.authentication_api.domain.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenStore tokenStore;

    private final JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/auth/login")) {
            filterChain.doFilter(request, response);
        } else if (request.getServletPath().equals("/auth/refresh")) {
            if (jwtUtils.validateToken(request)) {
                try {
                    tokenStore.validateRefreshToken(request);
                    User user = (User) customUserDetailsService.loadUserByUsername(jwtUtils.refreshUsername(request));
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), TokenModel.fromToken(tokenStore.generate(user, request)));
                } catch (Exception ex) {
                    response.setStatus(SC_FORBIDDEN);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of("errorMessage", ex.getMessage()));
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token");
            }
        } else {
            if (jwtUtils.validateToken(request)) {
                try {
                    tokenStore.validateAccessToken(request);
                    UsernamePasswordAuthenticationToken authenticationToken = jwtUtils.tokenAuthenticator(request);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception ex) {
                    response.setStatus(SC_FORBIDDEN);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of("errorMessage", ex.getMessage()));
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
