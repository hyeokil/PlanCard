package com.ssafy.backend.domain.alarm.repository;

import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface AlarmRepositoryCustom {
    Slice<AlarmDto> findAlarmSliceByMemberId(Long memberId, Pageable pageable);
}

