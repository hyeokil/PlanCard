package com.ssafy.backend.domain.card.repository;


import com.ssafy.backend.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>> getCardsByPlanId(Long planId);

    @Override
    Optional<Card> findById(Long cardId);

    @Query(value = "select c from Card c where c.plan.id = :planId and c.place.id = :placeId")
    Card findCardByPlanAndPlace(Long planId, Long placeId);

    @Override
    void deleteById(Long cardId);
}
