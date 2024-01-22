package com.ssafy.backend.global.exception;

import com.ssafy.backend.global.common.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ssafy.backend.global.exception.GlobalError.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 권한없는 사용자 요청 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity accessDeniedExceptionHandle(AccessDeniedException exception) {
        log.warn("인가에러 ex : {}", exception.getStackTrace());

        return ResponseEntity.status(FORBIDDEN_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, FORBIDDEN_EXCEPTION.getErrorMessage()));
    }

    /**
     * 인증되지 않은 사용자 요청 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authenticationExceptionHandle(AuthenticationException exception) {
        log.warn("인증에러 ex : {}", exception.getStackTrace());

        return ResponseEntity.status(AUTHENTICATION_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, AUTHENTICATION_EXCEPTION.getErrorMessage()));
    }

    /**
     * 기본 에러 처리
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity defaultExceptionHandle(Exception exception) {
        log.warn("기본에러 ex : {}", exception.getStackTrace());

        return ResponseEntity.status(DEFAULT_EXCEPTION.getHttpStatus())
                .body(Message.fail(null, DEFAULT_EXCEPTION.getErrorMessage()));
    }
}
