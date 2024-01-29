package com.ssafy.backend.domain.card.service;

import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.dto.CardUpdateMemoDto;
import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.card.repository.CardRepository;
import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.place.repository.PlaceInfoRepository;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            log.info(findPlace.toString());
            log.info(currentPlan.toString());
            cardRepository.save(cardCreateRequestDto.toEntity(findPlace.get(), currentPlan.get()));
        }
    }

    @Override
    public List<Card> getCardsByPlanId(Long planId) {
        Optional<List<Card>> cards = cardRepository.getCardsByPlanId(planId);
        return cards.orElse(null);

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
