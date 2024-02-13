package com.ssafy.backend.domain.plan.service;

import com.ssafy.backend.domain.plan.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanDetailListResponseDto;

import java.util.List;

public interface PlanDetailService {
    //상세 계획 생성 수정
    void updatePlanDetail(Long planId, List<PlanDetailCreateRequestDto> planDetailCreateRequestDtoList);
    //상세 계획 리스트 조회
    List<PlanDetailListResponseDto> getPlanDetailList(Long planId);

}