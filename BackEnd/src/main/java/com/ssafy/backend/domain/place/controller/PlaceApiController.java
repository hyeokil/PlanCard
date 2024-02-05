package com.ssafy.backend.domain.place.controller;

import com.ssafy.backend.domain.place.service.PlaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class PlaceApiController {

    @Value("${public-data.key}")
    private String key;

    private final PlaceService placeService;

    @GetMapping("/open-api")
    @Transactional
    public void getData() throws IOException {

        String stringUrl = "http://openapi.seoul.go.kr:8088/" + key + "/json/" + "TbVwRestaurants/1/1000";
        log.info(stringUrl);

        URL url = new URL(stringUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
//        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
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
            placeService.registPlace(sb.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

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

}
