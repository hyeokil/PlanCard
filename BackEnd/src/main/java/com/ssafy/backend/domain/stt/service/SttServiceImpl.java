package com.ssafy.backend.domain.stt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.sound.sampled.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final PlaceInfoRepository placeInfoRepository;

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
        HashMap<String, String> map = objectMapper.readValue(response.body().string(), HashMap.class);

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
//
        HttpUrl.Builder httpBuilder = HttpUrl.get("https://openapi.vito.ai/v1/transcribe:streaming").newBuilder();
        httpBuilder.addQueryParameter("sample_rate", "8000");
        httpBuilder.addQueryParameter("encoding", "LINEAR16");
        httpBuilder.addQueryParameter("use_itn", "true");
        httpBuilder.addQueryParameter("use_disfluency_filter", "true");
        httpBuilder.addQueryParameter("use_profanity_filter", "true");
//
        String url = httpBuilder.toString().replace("https://", "wss://");

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        RTZRWebSocketListener webSocketListener = new RTZRWebSocketListener(placeInfoRepository, cardService, planId);
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
        if (!targetDataLines.containsKey(planId)) {
            AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
            TargetDataLine line = AudioSystem.getTargetDataLine(audioFormat);
            line.open(audioFormat);
            line.start();
            targetDataLines.put(planId, line);

            // 오디오 캡처 및 전송 로직.
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (!Thread.currentThread().isInterrupted()) {
                    int bytesRead = line.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        rtzrWebSocket.send(ByteString.of(buffer, 0, bytesRead));
                    }
                }
            }).start();
        }

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
    public void processAudioData(byte[] audioData) throws IOException {
        //audioData 출력
//        log.info("audioData: {}", audioData);

        // STT 서비스 호출 및 텍스트 변환 로직 구현
        // 예시 코드는 실제 STT API 호출 과정을 단순화하여 표현합니다.
        String text = callSttService(audioData);

        // 변환된 텍스트 처리 로직 (예: 데이터베이스 저장, 로깅 등)
        log.info("Converted text: {}", text);

        // 변환된 텍스트를 기반으로 필요한 처리 수행
        // 예: 특정 조건에 맞는 데이터 처리, 응답 메시지 전송 등
    }

    //바이트 배열 wav파일로 변경 메서드
    public static void bytesToWav(byte[] byteArray, String outputFilePath, int sampleRate, int sampleSizeInBits, int channels) throws IOException {
        // Define AudioFormat
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, true, false);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
             AudioInputStream ais = new AudioInputStream(bais, format, byteArray.length / format.getFrameSize())) {
            // Write to WAV file
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(outputFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //전사 요청으로 반환 받은 id만 추출하는 메서드
    public static String extractID(String input) {
        String pattern = "\"id\":\"([^\"]+)\"";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);

        if (m.find()) {
            return m.group(1); // 첫 번째 그룹을 반환합니다.
        } else {
            return null; // 매칭되는 부분이 없을 경우 null을 반환합니다.
        }
    }

    //현재 음성 요청 상태값 반환받기 위한 메서드
    private static String extractStatus(String response) {
        // 여기서 response에서 status 추출하는 로직을 작성
        // JSON 파싱 등을 이용하여 추출하면 됩니다.
        // 예를 들어, Gson 라이브러리를 사용하면 간편하게 JSON을 파싱할 수 있습니다.
        // 이 예시에서는 간단하게 "status" 문자열을 찾아서 추출하는 것으로 가정합니다.
        int startIndex = response.indexOf("\"status\":\"") + "\"status\":\"".length();
        int endIndex = response.indexOf("\"", startIndex);
        log.info("현재 status: " + response.substring(startIndex, endIndex));
        return response.substring(startIndex, endIndex);
    }

    public static String pollUntilCompletion(String id, String accessToken) throws IOException {
        String status = "";
        String response = "";

        while (!status.equals("completed") && !status.equals("failed")) {
            try {
                // API 호출
                URL url = new URL("https://openapi.vito.ai/v1/transcribe/" + id);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setRequestProperty("accept", "application/json");
                httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);

                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
                response = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                // status 추출
                status = extractStatus(response);

                // 일정 시간 대기
                Thread.sleep(5000); // 5초 대기 (원하는 대기 시간으로 수정 가능)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("status 변경되어 polling 종료");

        log.info("json 파일에서 음성만 추출");
        // Gson을 사용하여 JSON을 파싱
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

        // "results" 안의 "utterances" 배열 추출
        JsonArray utterances = jsonObject.getAsJsonObject("results").getAsJsonArray("utterances");

        // "utterances" 배열에서 "msg" 값 추출
        StringBuilder sb = new StringBuilder();
        for (JsonElement element : utterances) {
            String msg = element.getAsJsonObject().get("msg").getAsString();
            log.info("추출된 음성 msg: " + msg);
            sb.append(msg);
        }
        log.info("완료된 텍스트 변환: " + sb);
        return sb.toString();
    }

    public static void saveByteArrayAsWav(byte[] byteArray, String filePath) throws IOException {
        // WAV 파일 헤더 작성
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // RIFF 헤더
            fos.write("RIFF".getBytes());
            fos.write(intToBytes(36 + byteArray.length)); // 파일 크기 (헤더 크기 + 데이터 크기)

            // WAVE 포맷
            fos.write("WAVE".getBytes());

            // fmt 청크
            fos.write("fmt ".getBytes());
            fos.write(intToBytes(16)); // fmt 청크 크기
            fos.write(shortToBytes(1)); // 포맷 ID (PCM = 1)
            fos.write(shortToBytes(1)); // 채널 수
            fos.write(intToBytes(44100)); // 샘플링 레이트
            fos.write(intToBytes(44100 * 2)); // 바이트율 (샘플링 레이트 * 바이트/샘플 * 채널 수)
            fos.write(shortToBytes(2)); // 블록 크기 (바이트/샘플 * 채널 수)
            fos.write(shortToBytes(16)); // 비트/샘플

            // 데이터 서브체크
            fos.write("data".getBytes());
            fos.write(intToBytes(byteArray.length)); // 데이터 크기

            // 데이터 쓰기
            fos.write(byteArray);
        }
    }

    private static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);
        return bytes;
    }

    private static byte[] shortToBytes(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        return bytes;
    }


    private String callSttService(byte[] audioData) throws IOException {
        // 여기에 STT API 호출 로직 구현
        // 이 부분은 실제 STT 서비스 제공자의 API 문서를 참조하여 구현합니다.
        // 이 예시에서는 변환된 가상의 텍스트를 반환합니다.

        //텍스트 음성 인식 stt 서비스에 파일 형태로 제공하기 위해 byte배열을 wav로 변환 후 텍스트 변환 처리합니다.
//        log.info(audioData.toString());
        bytesToWav(audioData, "audio.wav", 44100, 8, 1);
        try {
            saveByteArrayAsWav(audioData, "new.wav");
            log.info("WAV 파일이 생성되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String accessToken = getAccessToken();


        log.info("파일로 stt 요청 실행");
        URL useUrl = new URL("https://openapi.vito.ai/v1/transcribe");
        HttpURLConnection httpConn = (HttpURLConnection) useUrl.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty("Authorization", "Bearer " + accessToken);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=authsample");
        httpConn.setDoOutput(true);

        File file = new File("audio.wav");

        DataOutputStream outputStream;
        outputStream = new DataOutputStream(httpConn.getOutputStream());

        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        outputStream.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + "\r\n");
        outputStream.writeBytes("\r\n");

        FileInputStream in = new FileInputStream(file);
        byte[] fileBuffer = new byte[(int) file.length()];
        int fileBytesRead = -1;
        while ((fileBytesRead = in.read(fileBuffer)) != -1) {
            outputStream.write(fileBuffer, 0, fileBytesRead);
            outputStream.writeBytes("\r\n");
            outputStream.writeBytes("--authsample\r\n");
        }
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"config\"\r\n");
        outputStream.writeBytes("Content-Type: application/json\r\n");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("{}");
        outputStream.writeBytes("\r\n");
        outputStream.writeBytes("--authsample\r\n");
        outputStream.flush();
        outputStream.close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        s.close();
        System.out.println(response);

        log.info("파일로 stt 요청 후 반환 받은 id 값: " + response);
        log.info("파일로 stt 요청 종료");

        //id만 추출해서 url에 삽입
//        {"id":"RYpKxyCtTu65QJZ_PKaXKw"}
        String id = extractID(response);

        log.info("파일로 stt 요청 실행");

        return pollUntilCompletion(id, accessToken);
    }

}


/**
 * WebSocket 리스너 클래스로서, STT(음성 인식) 서비스로부터 오는 메시지를 처리합니다.
 * 해당 클래스는 WebSocket 이벤트를 감지하여 적절한 로직을 수행하게 됩니다.
 */
@Slf4j
class RTZRWebSocketListener extends WebSocketListener {

    private final PlaceInfoRepository placeInfoRepository;
    private final CardService cardService;
    private final Long planId;

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    /**
     * RTZRWebSocketListener 클래스의 생성자.
     *
     * @param placeInfoRepository 장소 정보에 대한 리포지토리
     * @param cardService         카드 서비스
     * @param planId              현재 처리 중인 여행 계획의 ID
     */
    public RTZRWebSocketListener(PlaceInfoRepository placeInfoRepository, CardService cardService, Long planId) {
        this.placeInfoRepository = placeInfoRepository;
        this.cardService = cardService;
        this.planId = planId;
    }

    /**
     * WebSocket 연결이 열릴 때 호출되는 메서드.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param response  연결에 대한 응답
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
     * @param text      수신된 텍스트 메시지
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
        List<Place> allPlaces = placeInfoRepository.findAll();
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
     * @param bytes     수신된 바이너리 데이터
     */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        log.info("Received bytes: {}", bytes.hex());
    }

    /**
     * WebSocket 연결이 종료될 때 호출되는 메서드.
     *
     * @param webSocket 현재 연결된 WebSocket
     * @param code      종료 코드
     * @param reason    종료 이유
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
     * @param t         발생한 예외
     * @param response  연결 실패에 대한 응답
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        log.error("WebSocket Failure: ", t);
    }

}

