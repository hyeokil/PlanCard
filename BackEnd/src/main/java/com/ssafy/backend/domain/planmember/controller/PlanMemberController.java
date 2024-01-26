package com.ssafy.backend.domain.planmember.controller;

import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.domain.planmember.service.PlanMemberService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/planmember")
public class PlanMemberController {

    private final PlanMemberService planMemberService;

    /**
     * 계획 초대 수락
     * @param planId 초대된 계획 id
     * @param loginActiveDto 현재 로그인한 회원정보
     * @return
     */
    @PostMapping("/{planId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> acceptPlan(@PathVariable("planId") Long planId,
                                                    @AuthenticationPrincipal MemberLoginActiveDto loginActiveDto) {
        planMemberService.acceptPlan(planId,loginActiveDto.getId());
        return ResponseEntity.ok().body(Message.success());

    }

}
