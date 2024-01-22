package com.ssafy.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalError {
    NOT_AUTHORITY_MEMBER_API(HttpStatus.FORBIDDEN, "해당 API 호출에 대한 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
    CERTIFICATION_NOT_TOKEN(HttpStatus.UNAUTHORIZED, "자격 증명이 되어 있지 않은 토큰입니다."),
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    AUTHENTICATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),
    DEFAULT_EXCEPTION(HttpStatus.BAD_REQUEST, "기본에러 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
