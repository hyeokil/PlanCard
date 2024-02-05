package com.ssafy.backend.domain.alarm.controller;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.service.AlarmService;
import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.common.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> createAlarm(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                     @RequestBody AlarmCreateRequestDto createRequestDto) {
        alarmService.createAlarm(loginActiveDto.getId(), createRequestDto);
        return ResponseEntity.ok().body(Message.success());
    }

//    @GetMapping("/list")
//    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
//    public ResponseEntity<Message<SliceResponse>> getAlarmList(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
//                                                                 Pageable pageable) {
//        SliceResponse alarms = alarmService.getAlarmList(loginActiveDto.getId(), pageable);
//        return ResponseEntity.ok().body(Message.success(alarms));
//    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<AlarmDto>>> getAlarmList(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                                @RequestParam(required = false) Long lastAlarmId,
                                                                @RequestParam(defaultValue = "4") int limit) {
        List<AlarmDto> alarmList = alarmService.getAlarmList(loginActiveDto.getId(), lastAlarmId, limit);
        return ResponseEntity.ok().body(Message.success(alarmList));
    }

    @PostMapping("/{alarmId}/{action}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> acceptAlarm(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                     @PathVariable Long alarmId, @PathVariable AlarmStatus action) {
        alarmService.handleAlarm(loginActiveDto.getId(), alarmId, action);
        return ResponseEntity.ok().body(Message.success());
    }
}
