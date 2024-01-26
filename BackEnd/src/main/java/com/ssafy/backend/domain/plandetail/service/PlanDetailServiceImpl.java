package com.ssafy.backend.domain.plandetail.service;

import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plandetail.entity.PlanDetail;
import com.ssafy.backend.domain.plandetail.repository.PlanDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanDetailServiceImpl implements PlanDetailService{

    private final PlanRepository planRepository;

    private final PlanDetailRepository planDetailRepository;
    @Override
    public void createPlanDetail(Long planId, PlanDetailCreateRequestDto planDetailCreateRequestDto) {
           Plan plan = planRepository.findById(planId).orElseThrow();
           PlanDetail planDetail = PlanDetail.builder()
                   .card(planDetailCreateRequestDto.getCard())
                   .plan(plan)
                   .orderNumber(planDetailCreateRequestDto.getOrderNumber())
                   .day(planDetailCreateRequestDto.getDay())
                   .memo(planDetailCreateRequestDto.getMemo())
                   .build();
           planDetailRepository.save(planDetail);
    }
}
