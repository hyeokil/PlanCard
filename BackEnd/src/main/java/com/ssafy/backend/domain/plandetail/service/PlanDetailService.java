package com.ssafy.backend.domain.plandetail.service;

import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailListResponseDto;

import java.util.List;

public interface PlanDetailService {
    //상세 계획 생성
    void createPlanDetail(Long planId, PlanDetailCreateRequestDto planDetailCreateRequestDto);

    List<PlanDetailListResponseDto> getPlanDetailList(Long planId);

}