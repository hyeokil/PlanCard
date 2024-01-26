package com.ssafy.backend.domain.card.repository;


import com.ssafy.backend.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>> getCardsByPlanId(Long planId);

    @Override
    Optional<Card> findById(Long cardId);
}
