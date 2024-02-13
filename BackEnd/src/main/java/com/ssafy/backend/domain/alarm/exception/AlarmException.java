package com.ssafy.backend.domain.alarm.exception;

import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {
    private final AlarmError errorCode;
    private final int status;
    private final String errorMessage;

    public AlarmException(AlarmError errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
