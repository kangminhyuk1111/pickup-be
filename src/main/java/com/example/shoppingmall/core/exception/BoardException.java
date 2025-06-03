package com.example.shoppingmall.core.exception;

public class BoardException extends ApplicationException {

    public BoardException(final CustomErrorCode customErrorCode) {
        super(customErrorCode);
    }
}
