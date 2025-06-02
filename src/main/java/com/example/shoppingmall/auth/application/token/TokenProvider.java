package com.example.shoppingmall.auth.application.token;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import io.jsonwebtoken.Claims;

public interface TokenProvider {

    String createToken(String payload);

    String parsePayload(String token);

    Long getUserId(String token);

    void validateToken(String token);

    String createTempToken(String oauthId, String nickname, ProviderType provider);

    Claims getClaims(String token);
}
