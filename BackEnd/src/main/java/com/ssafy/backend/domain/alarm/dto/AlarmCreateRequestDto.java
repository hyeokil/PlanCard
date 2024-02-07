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
public class AlarmCreateRequestDto {
    private static final String FRIEND_ALARM_CONTENT = " 님이 친구 요청을 보냈습니다.";
    private static final String CONFERENCE_ALARM_CONTENT = " 님이 회의를 시작하였습니다.";

    private Long toMemberId;    // 알람을 받는 멤버의 ID
    private AlarmType type; // 알람 유형
    private String content; // 알랑 내용

    public Alarm toEntity(Member fromMember, Member toMember) {
        if (type.equals(AlarmType.FRIEND)) {
            content = FRIEND_ALARM_CONTENT;
        } else {
            content = CONFERENCE_ALARM_CONTENT;
        }

        return Alarm.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .type(type)
                .content(content)
                .status(AlarmStatus.UNREAD)
                .build();
    }
}
