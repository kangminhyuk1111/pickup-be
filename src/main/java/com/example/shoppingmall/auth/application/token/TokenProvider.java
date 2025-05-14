package com.example.shoppingmall.auth.application.token;

public interface TokenProvider {

    String createToken(String payload);

    String parsePayload(String token);

    Long getUserId(String token);

    void validateToken(String token);
}
