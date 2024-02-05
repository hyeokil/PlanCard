package com.ssafy.backend.domain.alarm.repository;

import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;


public interface AlarmRepositoryCustom {
    Slice<AlarmDto> findAlarmSliceByMemberId(Long memberId, Pageable pageable);

    List<AlarmDto> findAlarmsAfterId(Long memberId, Long lastAlarmId, int limit);
}

