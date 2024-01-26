package com.ssafy.backend.domain.plandetail.dto;

import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plandetail.entity.PlanDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetailCreateRequestDto {

    private Card card;

    private Plan plan;

    private int orderNumber;

    private int day;

    private String memo;


}
