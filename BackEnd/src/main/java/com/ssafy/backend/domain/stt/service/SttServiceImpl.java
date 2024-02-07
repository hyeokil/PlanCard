package com.ssafy.backend.domain.stt.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.service.CardService;
import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.place.repository.PlaceInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttServiceImpl implements SttService {

    private final PlaceInfoRepository placeInfoRepository;

    private final CardService cardService;
    private volatile boolean isSttRunning = true;

    @Value("${stt.id}")
    String client_id;

    @Value("${stt.secret}")
    String client_secret;
    @Override
    public void stopStt() {
        isSttRunning = false;
    }
    public String getAccessToken() throws IOException {


        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", client_id)
                .add("client_secret", client_secret)
                .build();

        Request request = new Request.Builder()
                .url("https://openapi.vito.ai/v1/authenticate")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> map = objectMapper.readValue(response.body().string(), HashMap.class);

        return map.get("access_token");

    }

    @Override
    public void startStt(byte[] audioData) throws IOException, LineUnavailableException {
        isSttRunning = true;

        String accessToken = getAccessToken();


        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder httpBuilder = HttpUrl.get("https://openapi.vito.ai/v1/transcribe:streaming").newBuilder();
        httpBuilder.addQueryParameter("sample_rate","8000");
        httpBuilder.addQueryParameter("encoding","LINEAR16");
        httpBuilder.addQueryParameter("use_itn","true");
        httpBuilder.addQueryParameter("use_disfluency_filter","true");
        httpBuilder.addQueryParameter("use_profanity_filter","true");

        String url = httpBuilder.toString().replace("https://", "wss://");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer "+ accessToken)
                .build();

        RTZRWebSocketListener webSocketListener = new RTZRWebSocketListener(cardService, placeInfoRepository);
        WebSocket rtzrWebSocket = client.newWebSocket(request, webSocketListener);

        // 오디오 형식 설정
        AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);

        // 타겟 데이터 라인 얻기
        TargetDataLine line = AudioSystem.getTargetDataLine(audioFormat);

        line.open(audioFormat);
        line.start();

        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] buffer = new byte[1024];
        while (isSttRunning) {
            int bytesRead = line.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                rtzrWebSocket.send(ByteString.of(buffer, 0, bytesRead));
            }
        }
        line.stop();
        line.close();
        rtzrWebSocket.close(1000, "STT 프로세스 중지됨");


    }
}

class RTZRWebSocketListener extends WebSocketListener {
    private static final Logger logger = Logger.getLogger(RTZRWebSocketListener.class.getName());
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private static void log(Level level, String msg, Object... args) {
        logger.log(level, msg, args);
    }

    private final CardService cardService;


    private final PlaceInfoRepository placeInfoRepository;

    public RTZRWebSocketListener(CardService cardService, PlaceInfoRepository placeInfoRepository) {
        this.cardService = cardService;
        this.placeInfoRepository = placeInfoRepository;
    }
    private static int i = 0;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log(Level.INFO, "Open " + response.message());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println(i++);
        List<Place> allPlaces = placeInfoRepository.findAll();
        for (Place place : allPlaces) {
            String placeName = place.getName().toLowerCase(); // 대소문자 구분을 피하기 위해 소문자로 변환


            if (text.toLowerCase().contains(placeName)) {
                // 일치하는 name이 발견되면 해당 place의 id를 추출
                Long placeId = place.getId();
                cardService.createCard(1L,placeId,new CardCreateRequestDto(""));
                System.out.println("Found place with ID: " + placeId);
            }
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        System.out.println(bytes.hex());

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        log(Level.INFO, "Closing", code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();

    }

}