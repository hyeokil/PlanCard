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
public class AlarmFriendRequestDto {
    private static final String FRIEND_ALARM_CONTENT = " 님이 친구 요청을 보냈습니다.";

    private String email;
    private AlarmType type;

    public Alarm toEntity(Member fromMember, Member toMember) {

        return Alarm.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .type(type)
                .content(fromMember.getNickname() + FRIEND_ALARM_CONTENT)
                .status(AlarmStatus.UNREAD)
                .build();
    }
}
