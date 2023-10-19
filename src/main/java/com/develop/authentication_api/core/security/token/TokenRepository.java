package com.develop.authentication_api.core.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsTokenByAccessToken(String accessToken);

    Boolean existsTokenByRefreshToken(String refreshToken);
}
