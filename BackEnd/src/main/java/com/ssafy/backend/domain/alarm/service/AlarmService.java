package com.ssafy.backend.domain.alarm.service;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlarmService {
    void createAlarm(Long fromMemberId, AlarmCreateRequestDto createRequestDto);

    List<AlarmDto> getAlarmList(Long memberId, Pageable pageable);

    void acceptAlarm(Long memberId, Long alarmId);
}
