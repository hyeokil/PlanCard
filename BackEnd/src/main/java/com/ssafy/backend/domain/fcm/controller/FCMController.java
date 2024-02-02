package com.ssafy.backend.domain.fcm.controller;

import com.ssafy.backend.domain.fcm.dto.FCMTokenDto;
import com.ssafy.backend.domain.fcm.service.FCMService;
import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;

    /**
     * FCM Token 저장
     * @param loginActiveDto
     * @param fcmTokenDto FCM Token
     * @return
     */
    @PostMapping("/token")
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity saveFCMToken(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                       @RequestBody FCMTokenDto fcmTokenDto) {

        fcmService.saveFCMToken(loginActiveDto.getId(), fcmTokenDto.getToken());

        return ResponseEntity.ok(Message.success());
    }

    /**
     * FCM Token 삭제
     * @param loginActiveDto
     * @return
     */
    @DeleteMapping("/token")
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity deleteFCMToken(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto) {

        fcmService.deleteFCMToken(loginActiveDto.getId());

        return ResponseEntity.ok(Message.success());
    }
}
