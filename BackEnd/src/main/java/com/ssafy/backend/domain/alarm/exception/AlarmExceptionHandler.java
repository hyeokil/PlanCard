package com.ssafy.backend.domain.alarm.exception;

import com.ssafy.backend.global.common.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AlarmExceptionHandler {
    @ExceptionHandler(AlarmException.class)
    public ResponseEntity<Message<Void>> alarmException(AlarmException e) {
        log.error("{} is occurred", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(Message.fail(null, e.getErrorMessage()));
    }
}
