package com.ssafy.backend.domain.alarm.controller;

import com.ssafy.backend.domain.alarm.dto.AlarmCreateRequestDto;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.service.AlarmService;
import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.common.dto.Message;
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

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<AlarmDto>>> getAlarmList(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                                 Pageable pageable) {
        List<AlarmDto> alarmList = alarmService.getAlarmList(loginActiveDto.getId(), pageable);
        return ResponseEntity.ok().body(Message.success(alarmList));
    }

    @PostMapping("/accept/{alarmId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> acceptAlarm(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                     @PathVariable Long alarmId) {
        alarmService.acceptAlarm(loginActiveDto.getId(), alarmId);
        return ResponseEntity.ok().body(Message.success());
    }
}
