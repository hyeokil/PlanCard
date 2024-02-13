package com.ssafy.backend.domain.alarm.service;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.dto.AlarmFriendRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmPlanRequestDto;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.global.common.dto.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlarmService {
    void createAlarm(Long fromMemberId, AlarmCreateRequestDto createRequestDto);

//    SliceResponse getAlarmList(Long memberId, Pageable pageable);

    void friendRequestAlarm(Long fromMemberId, AlarmFriendRequestDto friendRequestDto);

    void planRequestAlarm(Long fromMemberId, List<AlarmPlanRequestDto> planRequestDtoList);

    List<AlarmDto> getAlarmList(Long memberId, Long lastAlarmId, int limit);

    void handleAlarm(Long memberId, Long alarmId, AlarmStatus action);

    void deleteAlarmList(Long memberId);
}
