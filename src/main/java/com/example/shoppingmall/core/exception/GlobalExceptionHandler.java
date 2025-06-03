package com.example.shoppingmall.core.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CustomErrorResponse> handleApplicationException(ApplicationException e) {
        CustomErrorResponse errorResponse = CustomErrorResponse.of(e.getCustomErrorCode());
        return ResponseEntity.status(e.getCustomErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorizationException(AuthorizationException e) {
        CustomErrorResponse errorResponse = CustomErrorResponse.of(e.getCustomErrorCode());
        return ResponseEntity.status(e.getCustomErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<CustomErrorResponse> handleBoardException(BoardException e) {
        CustomErrorResponse errorResponse = CustomErrorResponse.of(e.getCustomErrorCode());
        return ResponseEntity.status(e.getCustomErrorCode().getStatus()).body(errorResponse);
    }
}
