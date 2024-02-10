package com.ssafy.backend.domain.card.service;

import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.dto.CardResponseDto;
import com.ssafy.backend.domain.card.dto.CardUpdateMemoDto;
import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.card.repository.CardRepository;
import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.place.repository.PlaceInfoRepository;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final PlanRepository planRepository;
    private final PlaceInfoRepository placeInfoRepository;

    @Override
    public void createCard(Long planId, Long placeId, CardCreateRequestDto cardCreateRequestDto) {
        Optional<Plan> currentPlan = planRepository.findById(planId);
        Optional<Place> findPlace = placeInfoRepository.findById(placeId);
        if (currentPlan.isPresent() & findPlace.isPresent()) {
//            log.info(findPlace.toString());
//            log.info(currentPlan.toString());
            cardRepository.save(cardCreateRequestDto.toEntity(findPlace.get(), currentPlan.get()));
        }
    }

    @Override
    public List<CardResponseDto> getCardsByPlanId(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("해당 여행 계획이 없습니다."));

        // Plan이 존재하는 경우, Card 목록을 가져옵니다.
        List<Card> cards = cardRepository.getCardsByPlanId(plan.getId())
                .orElse(Collections.emptyList()); // 카드가 없는 경우 빈 리스트를 반환합니다.

        List<CardResponseDto> result = new ArrayList<>();
        for (Card card : cards) {
            result.add(new CardResponseDto(card));
        }
        return result;
    }

    @Override
    public Card findById(Long cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        return card.orElse(null);
    }

    @Override
    public void updateMemo(Long planId, Long cardId, String updateMemo, CardUpdateMemoDto cardUpdateMemoDto) {
        Card findCard = cardRepository.findCardByPlanAndPlace(planId, cardId);
        findCard.updateMemo(updateMemo);
        cardRepository.save(findCard);
    }

    @Override
    public void deleteById(Long cardId) {
        cardRepository.deleteById(cardId);
    }


}
