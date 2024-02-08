package com.ssafy.backend.domain.alarm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AlarmError {
    CANNOT_FRIEND_REQUEST_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게는 친구 요청을 보낼 수 없습니다."),
    ALREADY_FRIEND_OR_REQUESTED(HttpStatus.BAD_REQUEST, "이미 친구이거나 친구 요청을 보낸 상태입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
