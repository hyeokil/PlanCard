package com.ssafy.backend.domain.card.dto;

import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDto {

    private Long cardId;

    private String placeName;

    private String placeImage;

    private String placeAddress;

    private String memo;

    private Double latitude;

    private Double longitude;

    // Card 엔티티를 받아서 필드를 초기화하는 생성자
    public CardResponseDto(Card card) {
        this.cardId = card.getId();
        this.placeName = card.getPlace().getName();
        this.placeImage = card.getPlace().getImage();
        this.placeAddress = card.getPlace().getAddress();
        this.memo = card.getMemo();
        this.latitude = card.getPlace().getLatitude();
        this.longitude = card.getPlace().getLogitude();
    }

}
