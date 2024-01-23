package com.ssafy.backend.domain.friend.controller;


import com.ssafy.backend.domain.friend.service.FriendService;
import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {
    private final FriendService friendService;

    /**
     * 친구추가 수락
     * @param friendId 친구 id
     * @param loginActiveDto 현재 로그인한 회원정보
     * @return
     */
    @PostMapping("/{friendId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity acceptFriend(@PathVariable Long friendId,
                                       @AuthenticationPrincipal MemberLoginActiveDto loginActiveDto) {

        friendService.accept(loginActiveDto.getId(), friendId);

        return ResponseEntity.ok(Message.success());
    }
}
