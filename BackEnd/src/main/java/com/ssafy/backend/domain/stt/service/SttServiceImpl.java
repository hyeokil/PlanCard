package com.ssafy.backend.domain.stt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.service.CardService;
import com.ssafy.backend.domain.place.entity.Place;
import com.ssafy.backend.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class SttServiceImpl implements SttService {

    @Value("${stt.id}")
    String client_id;

    @Value("${stt.secret}")
    String client_secret;

    private final Map<Long, WebSocket> webSocketSessions = new ConcurrentHashMap<>();

    private final Map<Long, TargetDataLine> targetDataLines = new ConcurrentHashMap<>();

    private final PlaceRepository placeRepository;

    private final CardService cardService;


    @Override
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
    public void startTranscribe(Long planId) throws IOException, LineUnavailableException {

        // 중복 실행 방지 로직 추가...
        if (webSocketSessions.containsKey(planId)) {
            log.info("STT session for planId: {} is already running.", planId);
            return;
        }

        String accessToken = getAccessToken();
//        log.info(accessToken);

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

        RTZRWebSocketListener webSocketListener = new RTZRWebSocketListener(placeRepository, cardService, planId);
        WebSocket rtzrWebSocket = client.newWebSocket(request, webSocketListener);
        webSocketSessions.put(planId, rtzrWebSocket);

//        // 오디오 형식 설정
//        AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
//
//        // 타겟 데이터 라인 얻기
//        TargetDataLine line = AudioSystem.getTargetDataLine(audioFormat);
//        line.open(audioFormat);
//        line.start();
//
//
//        byte[] buffer = new byte[1024];
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        while (true) {
//            int bytesRead = line.read(buffer, 0, buffer.length);
//            if (bytesRead > 0) {
//                rtzrWebSocket.send(ByteString.of(buffer, 0, bytesRead));
//            }
//        }

        // 타겟 데이터 라인 생성 및 관리
//        if (!targetDataLines.containsKey(planId)) {
//            AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
//            TargetDataLine line = AudioSystem.getTargetDataLine(audioFormat);
//            line.open(audioFormat);
//            line.start();
//            targetDataLines.put(planId, line);
//
//            // 오디오 캡처 및 전송 로직.
//            new Thread(() -> {
//                byte[] buffer = new byte[1024];
//                while (!Thread.currentThread().isInterrupted()) {
//                    int bytesRead = line.read(buffer, 0, buffer.length);
//                    if (bytesRead > 0) {
//                        rtzrWebSocket.send(ByteString.of(buffer, 0, bytesRead));
//                    }
//                }
//            }).start();
//        }

    }

    /**
     * 특정 planId에 대한 STT 세션을 종료합니다.
     *
     * @param planId 종료할 STT 세션의 planId
     */
    @Override
    public void stopStt(Long planId) {
        WebSocket webSocket = webSocketSessions.get(planId);
        if (webSocket != null) {
            webSocket.close(1000, "STT session stopped");
            webSocketSessions.remove(planId);
        }

        // 타겟 데이터 라인 종료
        TargetDataLine line = targetDataLines.get(planId);
        if (line != null) {
            line.stop();
            line.close();
            targetDataLines.remove(planId);
        }

        log.info("Stopped STT session for planId: {}", planId);
    }

    // 오디오 데이터를 STT 서비스로 전송하고 변환된 텍스트를 처리
    @Override
    public void processAudioData(byte[] audioData) {


        // STT 서비스 호출 및 텍스트 변환 로직 구현
        // 예시 코드는 실제 STT API 호출 과정을 단순화하여 표현합니다.
        callSttService(audioData);


    }

    private void callSttService(byte[] audioData) {
        Long planId = 1L;
        WebSocket webSocket = webSocketSessions.get(planId);
        if (webSocket != null) {
            // Convert byte array to ByteString and send it via WebSocket
            webSocket.send(okio.ByteString.of(audioData));
            log.info("Sent audio data for planId: {}", planId);
        } else {
            log.error("No WebSocket session found for planId: {}", planId);
        }

    }



}


/**
 * WebSocket 리스너 클래스로서, STT(음성 인식) 서비스로부터 오는 메시지를 처리합니다.
 * 해당 클래스는 WebSocket 이벤트를 감지하여 적절한 로직을 수행하게 됩니다.
 */
@Slf4j
class RTZRWebSocketListener extends WebSocketListener {

    private final PlaceRepository placeRepository;
    private final CardService cardService;
    private final Long planId;

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    /**
     * RTZRWebSocketListener 클래스의 생성자.
     *
     * @param placeRepository 장소 정보에 대한 리포지토리
     * @param cardService 카드 서비스
     * @param planId 현재 처리 중인 여행 계획의 ID
     */
    public RTZRWebSocketListener(PlaceRepository placeRepository, CardService cardService, Long planId) {
        this.placeRepository = placeRepository;
        this.cardService = cardService;
        this.planId = planId;
    }

    /**
     * WebSocket 연결이 열릴 때 호출되는 메서드.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param response 연결에 대한 응답
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info("WebSocket Opened: {}", response.message());
    }

    /**
     * 텍스트 메시지를 받았을 때 호출되는 메서드.
     * 여기서는 STT 서비스로부터 전송된 텍스트를 기반으로 여행지에 대한 카드를 생성합니다.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param text 수신된 텍스트 메시지
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        log.info("Received text: {}", text);
        processTextMessage(text);
    }

    /**
     * 수신된 텍스트 메시지를 처리하여 해당 텍스트와 일치하는 여행지를 찾고,
     * 해당 여행지에 대한 카드를 생성하는 메서드.
     *
     * @param text 처리할 텍스트 메시지
     */
    private void processTextMessage(String text) {
        List<Place> allPlaces = placeRepository.findAll();
        for (Place place : allPlaces) {
            String placeName = place.getName().toLowerCase();
            if (text.toLowerCase().contains(placeName)) {
                Long placeId = place.getId();
                cardService.createCard(planId, placeId, new CardCreateRequestDto(""));
                log.info("Created card for place with ID: {} Name: {}", placeId, placeName);
                break;
            }
        }
    }

    /**
     * 바이너리 메시지를 받았을 때 호출되는 메서드.
     * 현재 구현에서는 바이너리 메시지를 처리하지 않습니다.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param bytes 수신된 바이너리 데이터
     */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        log.info("Received bytes: {}", bytes.hex());
    }

    /**
     * WebSocket 연결이 종료될 때 호출되는 메서드.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param code 종료 코드
     * @param reason 종료 이유
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        log.info("WebSocket Closing: Code = {}, Reason = {}", code, reason);
    }

    /**
     * WebSocket 연결 실패시 호출되는 메서드.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param t 발생한 예외
     * @param response 연결 실패에 대한 응답
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        log.error("WebSocket Failure: ", t);
    }

}

