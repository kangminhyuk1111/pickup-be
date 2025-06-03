package com.example.shoppingmall.core.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final CustomErrorCode customErrorCode;

    public ApplicationException(CustomErrorCode customErrorCode) {
        super();
        this.customErrorCode = customErrorCode;
    }
}