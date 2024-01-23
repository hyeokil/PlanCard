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
//    private String business_hours; //운영시간
//    private String menu; //대표메뉴

    public Place toEntity() {
        return Place.builder()
                .name(name)
                .address(address)
                .category(category)
                .image(img)
                .build();
    }
}
