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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanDetailServiceImpl implements PlanDetailService{

    private final PlanRepository planRepository;
    private final CardRepository cardRepository;
    private final PlanDetailRepository planDetailRepository;
    @Override
    public void updatePlanDetail(Long planId,
                                          List<PlanDetailCreateRequestDto> planDetailCreateRequestDtoList) {
        if (planDetailCreateRequestDtoList == null || planDetailCreateRequestDtoList.isEmpty()) {
            throw new RuntimeException("생성할 여행 상세 계획이 없습니다.");
        }
        Plan plan = planRepository.findById(planId).orElseThrow();
        Map<Long, PlanDetail> existingPlanDetails = planDetailRepository.findByPlanId(planId)
                .stream()
                .collect(Collectors.toMap(PlanDetail::getId, planDetail -> planDetail));
        for (PlanDetailCreateRequestDto newplanDetail : planDetailCreateRequestDtoList) {
            if (newplanDetail.getId() == null) {
                Card card = cardRepository.findById(newplanDetail.getCardId()).orElseThrow();
                planDetailRepository.save(newplanDetail.toEntity(card, plan));
            } else {
                PlanDetail planDetail = planDetailRepository.findById(newplanDetail.getId()).orElseThrow();
                planDetail.update(newplanDetail.getOrderNumber(), newplanDetail.getDay());
                planDetailRepository.save(planDetail);
                existingPlanDetails.remove(newplanDetail.getId());
            }
        }
        planDetailRepository.deleteAll(existingPlanDetails.values());
    }

    @Override
    public List<PlanDetailListResponseDto> getPlanDetailList(Long planId) {
        List<PlanDetail> planDetails = planDetailRepository.findByPlanId(planId);

        List<PlanDetailListResponseDto> sortedPlanDetailList = planDetails.stream()
                .map(planDetail -> new PlanDetailListResponseDto(
                        planDetail.getId(),
                        planDetail.getCard().getId(),
                        planDetail.getCard().getPosition(),
                        planDetail.getCard().getMemo(),
                        planDetail.getCard().getPlace().getName(),
                        planDetail.getCard().getPlace().getAddress(),
                        planDetail.getOrderNumber(),
                        planDetail.getDay()))
                .sorted() // Comparable에 따라 정렬
                .collect(Collectors.toList());

        return sortedPlanDetailList;
    }
}
