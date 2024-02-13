package com.ssafy.backend.domain.alarm.dto;

import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlarmDto {
    private Long alarmId;
    private Long fromMemberId;
    private String fromMemberNickname;
    private Long toMemberId;
    private AlarmType type;
    private AlarmStatus status;
    private String content;
    private String url;
}
