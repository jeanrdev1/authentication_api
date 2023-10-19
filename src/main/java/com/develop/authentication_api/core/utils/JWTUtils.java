package com.develop.authentication_api.core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JWTUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private Integer jwtAccessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Integer jwtRefreshExpiration;

    public String generateAccessToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Date expiration = new Date(System.currentTimeMillis() + jwtAccessExpiration);
        return JWT.create().withSubject(user.getUsername()).withExpiresAt(expiration).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).withIssuer(request.getRequestURI()).withIssuedAt(new Date()).sign(algorithm);
    }

    public String generateRefreshToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Date expiration = new Date(System.currentTimeMillis() + jwtRefreshExpiration);
        return JWT.create().withSubject(user.getUsername()).withExpiresAt(expiration).withIssuer(request.getRequestURI()).sign(algorithm);
    }

    public Boolean validateToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        return StringUtil.isNotNull(token) && token.startsWith("Bearer ");
    }

    public UsernamePasswordAuthenticationToken tokenAuthenticator(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
    }

    public String refreshUsername(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}