package com.example.shoppingmall.core.exception;

public class AuthorizationException extends ApplicationException {

    public AuthorizationException(CustomErrorCode customErrorCode) {
        super(customErrorCode);
    }
}
