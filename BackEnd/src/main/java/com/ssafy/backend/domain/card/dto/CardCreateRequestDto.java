package com.ssafy.backend.domain.card.dto;

import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.plan.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardCreateRequestDto {

    private Long id;
    private String memo;

    public Card toEntity(Place place, Plan plan){
        return Card.builder()
                .place(place)
                .plan(plan)
                .memo(memo)
                .build();
    }


}
