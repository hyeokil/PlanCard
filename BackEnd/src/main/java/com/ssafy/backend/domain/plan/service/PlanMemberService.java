package com.ssafy.backend.domain.plan.service;

public interface PlanMemberService {
    //계획 초대 수락
    void acceptPlan(Long planId, Long memberId);

}
