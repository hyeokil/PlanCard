package com.ssafy.backend.domain.place.service;

import com.ssafy.backend.domain.place.dto.PlaceGetResponseDto;

import java.util.List;

public interface PlaceService {

    void registPlace(String placeList);

    void registPlace2(String placeList);

    List<PlaceGetResponseDto> searchPlaceByName(String name);


}
