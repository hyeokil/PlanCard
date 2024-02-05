package com.ssafy.backend.domain.plandetail.controller;

import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailListResponseDto;
import com.ssafy.backend.domain.plandetail.service.PlanDetailService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plan/{planId}")
public class PlanDetailController {
    private final PlanDetailService planDetailService;

    @PostMapping("/detail/update")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> updatePlanDetail(@PathVariable("planId") Long planId,
                                                          @RequestBody List<PlanDetailCreateRequestDto> planDetailCreateRequestDtoList) {
        planDetailService.updatePlanDetail(planId, planDetailCreateRequestDtoList);
        return ResponseEntity.ok().body(Message.success());


    }

    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<PlanDetailListResponseDto>>> getPlanDetailList(@PathVariable("planId") Long planId) {
        List<PlanDetailListResponseDto> planDetailList = planDetailService.getPlanDetailList(planId);
        return ResponseEntity.ok().body(Message.success(planDetailList));
    }


}
