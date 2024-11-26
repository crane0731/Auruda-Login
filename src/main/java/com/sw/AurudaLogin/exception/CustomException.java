package com.sw.AurudaLogin.exception;

import lombok.Getter;

//커스텀 예외클래스
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
