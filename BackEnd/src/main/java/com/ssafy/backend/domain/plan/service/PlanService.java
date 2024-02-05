package com.ssafy.backend.domain.plan.service;


import com.ssafy.backend.domain.plan.dto.MyPlanResponseDto;
import com.ssafy.backend.domain.plan.dto.PlanCreateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanDateUpdateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanNameUpdateRequestDto;

import java.util.List;

public interface PlanService {
    //여행 계획 생성
    Long createPlan(PlanCreateRequestDto createRequestDto);

    //여행 이름 수정
    void updatePlanName(Long planId, PlanNameUpdateRequestDto planNameUpdateRequestDto);

    //여행 날짜 수정
    void updatePlanDate(Long planId, PlanDateUpdateRequestDto planDateUpdateRequestDto);

    //로그인한 회원의 여행 계획 리스트 조회
    List<MyPlanResponseDto> getMyPlanList(Long memberId);

    //로그인한 회원의 여행 계획 조회
    MyPlanResponseDto getMyPlan(Long planId);
}
