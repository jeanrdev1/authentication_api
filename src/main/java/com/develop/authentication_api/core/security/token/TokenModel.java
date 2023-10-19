package com.develop.authentication_api.core.security.token;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TokenModel {

    private String accessToken;

    private String refreshToken;

    public static TokenModel fromToken(Token token) {
        TokenModel tokenModel = new TokenModel();
        BeanUtils.copyProperties(token, tokenModel);
        return tokenModel;
    }
}
