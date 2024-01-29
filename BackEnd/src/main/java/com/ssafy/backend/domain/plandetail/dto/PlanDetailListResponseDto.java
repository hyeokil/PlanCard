package com.ssafy.backend.domain.plandetail.dto;

import com.ssafy.backend.domain.card.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailListResponseDto {

    private String placeName;

    private String placeAddress;

    private int orderNumber;

    private int day;


}
