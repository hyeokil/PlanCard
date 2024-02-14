package com.ssafy.backend.domain.place.controller;

import com.ssafy.backend.domain.place.dto.PlaceGetResponseDto;
import com.ssafy.backend.domain.place.service.PlaceService;
import com.ssafy.backend.global.common.dto.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@RequestMapping("/api/v1/place")
@Slf4j
@RequiredArgsConstructor
public class PlaceController {

    @Value("${public-data.key}")
    private String key;

    private final PlaceService placeService;

    @GetMapping("/open-api2")
    //새로운 공공데이터 받기
    @Transactional
    public void getdata2() throws IOException {


        for (int i = 1; i <= 14; i++) {

            String stringUrl = "http://smarttour.junggu.seoul.kr//junggu/openapi/culture.do?pageIndex=" + i;

            URL url = new URL(stringUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;

            // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            //db저장
            try {
//                System.out.println(sb.toString());
                placeService.registPlace2(sb.toString());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<List<PlaceGetResponseDto>>> searchPlaceByName(@RequestParam String name) {
        List<PlaceGetResponseDto> placeSearchList = placeService.searchPlaceByName(name);
        return ResponseEntity.ok().body(Message.success(placeSearchList));
    }

}
