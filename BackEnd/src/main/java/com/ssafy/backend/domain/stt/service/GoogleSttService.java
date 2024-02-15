package com.ssafy.backend.domain.stt.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class GoogleSttService {
    private final Logger logger = LoggerFactory.getLogger(GoogleSttService.class);


    public void byteToWav(byte[] audioData, String filePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // 임시 WAV 파일 생성
        File tempFile = File.createTempFile("temp", ".wav");

        // WAV 파일 포맷 설정
        AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
        AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), audioFormat, audioData.length / audioFormat.getFrameSize());

        // WAV 파일 생성
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, tempFile);

        // 임시 WAV 파일을 목표 파일 경로로 복사
        try (InputStream in = new FileInputStream(tempFile); OutputStream out = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        // 임시 파일 삭제
        tempFile.delete();
    }


//    public String transcribe(MultipartFile audioFile) throws IOException {
    public String transcribe(byte[] audioFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
//        if (audioFile.isEmpty()) {
//            throw new IOException("Required part 'audioFile' is not present.");
//        }

        // 오디오 파일을 byte array로 decode

        //바이트 배열을 wav오디오 파일로 변환
        byteToWav(audioFile, "output.wav");

//        File file = new File("구 미국문화원이랑 대한문.wav");
        File file = new File("corona.wav");
        byte[] audioBytes = Files.readAllBytes(file.toPath());

        // 클라이언트 인스턴스화
        try (SpeechClient speechClient = SpeechClient.create()) {

            // 오디오 객체 생성
            ByteString audioData = ByteString.copyFrom(audioBytes);
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData)
                    .build();

//            log.info("변환 전 바이트 스트링 {}", audioData);
//            log.info("변환 전 오디오 {}", recognitionAudio);

            // 설정 객체 생성
            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("ko-KR")
//                            .setLanguageCode("en-US")
                            .setAudioChannelCount(1)
                            .build();

            // 오디오-텍스트 변환 수행
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            log.info("변환 후 {}", response.getResultsList());
            List<SpeechRecognitionResult> results = response.getResultsList();

            if (!results.isEmpty()) {
                // 주어진 말 뭉치에 대해 여러 가능한 스크립트를 제공. 0번(가장 가능성 있는)을 사용한다.
                SpeechRecognitionResult result = results.get(0);
                return result.getAlternatives(0).getTranscript();
            } else {
                logger.error("No transcription result found");
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
