package com.ssafy.backend.domain.alarm.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmStatus {
    ACCEPT, // 수락 - 이후 보이지 않음
    REJECT, // 거절
    UNREAD, // 안읽음
    READ; // 읽음

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
