package com.ssafy.backend.domain.plan.service;


import com.ssafy.backend.domain.plan.dto.MyPlanListResponseDto;
import com.ssafy.backend.domain.plan.dto.PlanCreateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanDateUpdateRequestDto;
import com.ssafy.backend.domain.plan.dto.PlanNameUpdateRequestDto;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.planmember.repository.PlanMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;



    @Override
    public Long createPlan(PlanCreateRequestDto createRequestDto) {
        Plan plan = planRepository.save(createRequestDto.toEntity());
        return plan.getId();
    }

    @Override
    public void updatePlanName(Long planId, PlanNameUpdateRequestDto planNameUpdateRequestDto) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new RuntimeException("해당 id의 Plan을 찾을 수 없습니다: " + planId));
        plan.updateName(planNameUpdateRequestDto.getName());
        planRepository.save(plan);

    }

    @Override
    public void updatePlanDate(Long planId, PlanDateUpdateRequestDto planDateUpdateRequestDto) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new RuntimeException("해당 id의 Plan을 찾을 수 없습니다: " + planId));
        plan.updateStartDate(planDateUpdateRequestDto.getStartDate());
        plan.updateEndDate(planDateUpdateRequestDto.getEndDate());
        planRepository.save(plan);

    }

    @Override
    public List<MyPlanListResponseDto> getMyPlanList(Long memberId) {
        List<Plan> plans = planRepository.findByPlanMembers_MemberId(memberId);
        return plans.stream()
                .map(plan -> new MyPlanListResponseDto(
                        plan.getId(),
                        plan.getName(),
                        plan.getStartDate(),
                        plan.getEndDate()))
                .collect(Collectors.toList());
    }


}
