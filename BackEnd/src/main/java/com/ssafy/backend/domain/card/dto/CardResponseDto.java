package com.ssafy.backend.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponseDto {

    private Long cardId;

    private String placeName;

    private String placeAdds;

    private String memo;



}
