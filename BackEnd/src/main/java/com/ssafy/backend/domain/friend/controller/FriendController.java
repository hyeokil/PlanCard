package com.ssafy.backend.domain.friend.controller;


import com.ssafy.backend.domain.friend.dto.AcceptDto;
import com.ssafy.backend.domain.friend.dto.FriendshipRequestDto;
import com.ssafy.backend.domain.friend.dto.RefuseDto;
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

    /**
     * 친구추가 요청 수락처리
     * @param acceptDto
     * @return
     */
    @PostMapping("/accept")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity acceptFriend(@RequestBody AcceptDto acceptDto) {

        friendshipService.accept(acceptDto.getAlarmId(), acceptDto.getOwnerId(), acceptDto.getFriendId());

        return ResponseEntity.ok(Message.success());
    }

    @PostMapping("/refuse")
    @PreAuthorize("hasAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity refuseFriend(@RequestBody RefuseDto refuseDto) {
        friendshipService.refuse(refuseDto.getAlarmId());
        return ResponseEntity.ok(Message.success());
    }

    @PostMapping("/request")
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity requestFriend(@RequestBody FriendshipRequestDto friendshipRequestDto) {
        friendshipService.requestFriendship(friendshipRequestDto.getOwnerId(), friendshipRequestDto.getFriendId());
        return ResponseEntity.ok(Message.success());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER') or hasAnyAuthority('ADMIN')")
    public ResponseEntity getFriends(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto, Pageable pageable) {

        SliceResponse friends = friendshipService.getFriends(loginActiveDto.getId(), pageable);

        return ResponseEntity.ok(Message.success(friends));
    }

}
