package com.ssafy.backend.domain.plandetail.service;

import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.card.repository.CardRepository;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailCreateRequestDto;
import com.ssafy.backend.domain.plandetail.dto.PlanDetailListResponseDto;
import com.ssafy.backend.domain.plandetail.entity.PlanDetail;
import com.ssafy.backend.domain.plandetail.repository.PlanDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanDetailServiceImpl implements PlanDetailService{

    private final PlanRepository planRepository;
    private final CardRepository cardRepository;

    private final PlanDetailRepository planDetailRepository;
    @Override
    public void createPlanDetail(Long planId, PlanDetailCreateRequestDto planDetailCreateRequestDto) {
           Plan plan = planRepository.findById(planId).orElseThrow();
           Card card = cardRepository.findById(planDetailCreateRequestDto.getCardId()).orElseThrow();
           planDetailRepository.save(planDetailCreateRequestDto.toEntity(card, plan));
    }

    @Override
    public List<PlanDetailListResponseDto> getPlanDetailList(Long planId) {
        List<PlanDetail> planDetails = planDetailRepository.findByPlanId(planId);

        return planDetails.stream()
                .map(planDetail -> new PlanDetailListResponseDto(
                        planDetail.getCard().getPlace().getName(),
                        planDetail.getCard().getPlace().getAddress(),
                        planDetail.getOrderNumber(),
                        planDetail.getDay()))
                .collect(Collectors.toList());
    }
}
