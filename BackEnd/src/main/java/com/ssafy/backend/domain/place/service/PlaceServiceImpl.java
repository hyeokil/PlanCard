package com.ssafy.backend.domain.place.service;

import com.ssafy.backend.domain.place.dto.PlaceRegisterDto;
import com.ssafy.backend.domain.place.repository.PlaceInfoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final String LANG = "LANG_CODE_ID"; //언어
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

                String lang = (String) object.get(LANG);
                String adds = (String) object.get(ADDRESS);

                //주소가 공백이 아니며 한글인 음식점만 고려합니다.
                if (lang.equals("ko") && !adds.isBlank()) {
                    // 관광지명
                    String name = (String) object.get(NAME);

                    PlaceRegisterDto dto = new PlaceRegisterDto();
                    dto.setName(name);
                    dto.setAddress(adds);

                    log.info(dto.getAddress());
                    placeInfoRepository.save(dto.toEntity());

                }
            }

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
