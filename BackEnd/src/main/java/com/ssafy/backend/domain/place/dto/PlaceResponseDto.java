package com.ssafy.backend.domain.place.dto;

import com.ssafy.backend.domain.place.entity.Place;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlaceResponseDto {

    private List<Place> places;

    public List<Place> toEntity(List<PlaceRegisterDto> placeRegisterDto) {
        return placeRegisterDto.stream()
                .map(PlaceRegisterDto::toEntity)
                .collect(Collectors.toList());
    }

}
