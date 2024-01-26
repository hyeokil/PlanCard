package com.ssafy.backend.domain.plandetail.service;

import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;

public interface PlanDetailService {
    //상세 계획 생성
    void createPlanDetail(Long planId, PlanDetailCreateRequestDto planDetailCreateRequestDto);

}
