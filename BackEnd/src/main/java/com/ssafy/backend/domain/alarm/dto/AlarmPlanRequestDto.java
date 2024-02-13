package com.ssafy.backend.domain.alarm.dto;

import com.ssafy.backend.domain.alarm.entity.Alarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmPlanRequestDto {
    private String planAlarmContent = " 여행 계획 참여 요청을 보냈습니다.";

    private Long friendId;
    private AlarmType type;
    private String planName;
    private String url;

    public Alarm toEntity(Member fromMember, Member toMember) {
        return Alarm.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .type(type)
                .content(fromMember.getName() + " 님이 " +  planName + planAlarmContent)
                .url(url)
                .status(AlarmStatus.UNREAD)
                .build();
    }
}
