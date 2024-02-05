package com.ssafy.backend.domain.place.dto;

import com.ssafy.backend.domain.place.entity.Place;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceRegisterDto {

    private Long id;
    private String name; //상호명
    private String address; //주소
    private String category; //카테고리
    private String img; //이미지
    private Double latitude; //위도
    private Double longitude; //경도

    public Place toEntity() {
        return Place.builder()
                .name(name)
                .address(address)
                .category(category)
                .image(img)
                .latitude(latitude)
                .logitude(longitude)
                .build();
    }
}
