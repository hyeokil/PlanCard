package com.ssafy.backend.domain.alarm.entity.enums;

import lombok.Getter;

@Getter
public enum AlarmType {
    FRIEND("친구요청", "님이 친구요청"),
    CONFERENCE("화상회의 초대", "화상회의 초대 요청"),
    PLAN("여행 계획 참여 요청", "여행 계획 초대 요청");

    private String title;
    private String content;

    private AlarmType(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
