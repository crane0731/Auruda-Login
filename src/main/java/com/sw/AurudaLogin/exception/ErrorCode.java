package com.sw.AurudaLogin.exception;

public enum ErrorCode {
    INVALID_REQUEST,//잘못된 요청입니다.
    UNAUTHORIZED,//인증이 필요합니다.
    FORBIDDEN,//접근 권한이 없습니다.
    NOT_FOUND,//리소스를 찾을 수 없습니다.
    METHOD_NOT_ALLOWED,//허용되지 않는 메서드입니다.
    CONFLICT,//충돌이 발생했습니다.
    UNPROCESSABLE_ENTITY,//처리할 수 없는 요청입니다.
    INTERNAL_SERVER_ERROR//서버 오류가 발생했습니다.
}
