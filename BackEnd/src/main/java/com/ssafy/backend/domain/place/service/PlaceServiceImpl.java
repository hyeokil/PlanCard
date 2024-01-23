package com.ssafy.backend.domain.place.service;

import com.ssafy.backend.domain.place.dto.PlaceRegisterDto;
import com.ssafy.backend.domain.place.repository.PlaceInfoRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final String NAME = "POST_SJ"; //상호명
    private final String ADDRESS = "ADDRESS"; //주소
    private final PlaceInfoRepository placeInfoRepository;

    public PlaceServiceImpl(PlaceInfoRepository placeInfoRepository) {
        this.placeInfoRepository = placeInfoRepository;
    }


    @Override
    public void registPlace(String placeList) {

        try {
            //json데이터 파싱
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(placeList);
            System.out.println(jsonObject);

            //row 데이터 뽑기
            JSONObject data = (JSONObject) jsonObject.get("TbVwRestaurants");
            JSONArray rowData = (JSONArray) data.get("row");

            for (Object obj : rowData) {
                JSONObject object = (JSONObject) obj;

                // 관광지명
                String name = (String) object.get(NAME);
                // 주소
                String adds = (String) object.get(ADDRESS);

                System.out.println("name = " + name);
                System.out.println("adds = " + adds);

                PlaceRegisterDto dto = new PlaceRegisterDto();
                dto.setName(name);
                dto.setAddress(adds);
                dto.setImg(null);

                System.out.println("dto = " + dto);
                placeInfoRepository.save(dto.toEntity());
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
