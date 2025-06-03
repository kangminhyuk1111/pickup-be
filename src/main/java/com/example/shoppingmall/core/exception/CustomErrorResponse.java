package com.example.shoppingmall.core.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse {

    private final int status;
    private final String errorCode;
    private final String errorMessage;

    private CustomErrorResponse(final int status, final String errorCode, final String errorMessage) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static CustomErrorResponse of(CustomErrorCode errorCode) {
        return new CustomErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
