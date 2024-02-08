package com.ssafy.backend.domain.plan.controller;

import com.ssafy.backend.domain.member.dto.MemberLoginActiveDto;
import com.ssafy.backend.domain.plan.dto.MyPlanResponseDto;
import com.ssafy.backend.domain.plan.dto.PlanCreateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanDateUpdateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanNameUpdateRequestDto;
import com.ssafy.backend.domain.plan.service.PlanService;
import com.ssafy.backend.domain.plan.service.PlanMemberService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plan")
public class PlanController {
    private final PlanService planService;
    private final PlanMemberService planMemberService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Long>> createPlan(@RequestBody PlanCreateRequestDto planCreateRequestDto,
                                                    @AuthenticationPrincipal MemberLoginActiveDto loginActiveDto) {

        Long planId = planService.createPlan(planCreateRequestDto);
        planMemberService.acceptPlan(planId,loginActiveDto.getId());
        return ResponseEntity.ok().body(Message.success(planId));
    }

    @PatchMapping("/update/{planId}/name")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> updatePlanName(@PathVariable("planId") Long planId,
                                                    @RequestBody PlanNameUpdateRequestDto planNameUpdateRequestDto) {
        planService.updatePlanName(planId, planNameUpdateRequestDto);
        return ResponseEntity.ok().body(Message.success());
    }

    @PatchMapping("/update/{planId}/date")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> updatePlanDate(@PathVariable("planId") Long planId,
                                                        @RequestBody PlanDateUpdateRequestDto planDateUpdateRequestDto) {
        planService.updatePlanDate(planId, planDateUpdateRequestDto);
        return ResponseEntity.ok().body(Message.success());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<MyPlanResponseDto>>> getMyPlanList(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto) {
        List<MyPlanResponseDto> myPlanList = planService.getMyPlanList(loginActiveDto.getId());
        return ResponseEntity.ok().body(Message.success(myPlanList));
    }

    @GetMapping("/{planId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<MyPlanResponseDto>> getMyPlan(@PathVariable("planId") Long planId) {
        MyPlanResponseDto myPlan = planService.getMyPlan(planId);
        return ResponseEntity.ok().body(Message.success(myPlan));
    }


}
