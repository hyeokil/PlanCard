package com.ssafy.backend.domain.card.service;


import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.dto.CardUpdateMemoDto;
import com.ssafy.backend.domain.card.entity.Card;

import java.util.List;

public interface CardService {

    void createCard(Long planId, Long placeId, CardCreateRequestDto cardCreateRequestDto);

    List<Card> getCardsByPlanId(Long planId);

    Card findById(Long cardId);

    void updateMemo(Long planId, Long PlaceId, String updateMemo, CardUpdateMemoDto cardUpdateMemoDto);

    void deleteById(Long cardId);



}
