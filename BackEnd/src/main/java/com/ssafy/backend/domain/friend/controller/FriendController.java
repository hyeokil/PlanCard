package com.ssafy.backend.domain.friend.controller;


import com.ssafy.backend.domain.friend.service.FriendshipService;
import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.common.dto.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {
    private final FriendshipService friendshipService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity getFriends(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto, Pageable pageable) {

        SliceResponse friends = friendshipService.getFriends(loginActiveDto.getId(), pageable);

        return ResponseEntity.ok(Message.success(friends));
    }

}
