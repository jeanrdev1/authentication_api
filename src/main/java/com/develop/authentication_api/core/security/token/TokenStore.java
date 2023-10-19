package com.develop.authentication_api.core.security.token;

import com.develop.authentication_api.core.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@AllArgsConstructor
public class TokenStore {

    private final TokenRepository tokenRepository;

    private final JWTUtils jwtUtils;

    public Token generate(User user, HttpServletRequest request) {
        Token token = new Token();
        token.setAccessToken(jwtUtils.generateAccessToken(user, request));
        token.setRefreshToken(jwtUtils.generateRefreshToken(user, request));
        return tokenRepository.save(token);
    }

    public void validateAccessToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(AUTHORIZATION);
        if (!tokenRepository.existsTokenByAccessToken(token.substring("Bearer ".length()))) {
            throw new Exception("Unregistered access token");
        }
    }

    public void validateRefreshToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader(AUTHORIZATION);
        if (!tokenRepository.existsTokenByRefreshToken(token.substring("Bearer ".length()))) {
            throw new Exception("Unregistered refresh token");
        }
    }
}
