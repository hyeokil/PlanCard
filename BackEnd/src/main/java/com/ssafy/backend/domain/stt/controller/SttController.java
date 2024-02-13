package com.ssafy.backend.domain.stt.controller;

import com.ssafy.backend.domain.stt.service.SttService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.LineUnavailableException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SttController {
    private final SttService sttService;

    @MessageMapping("/api/v1/audio") //websocket에서 제공하는 vue에서 메시지 받을 컨트롤러
    public void SttHandler(@Payload byte[] audioData) throws LineUnavailableException, IOException {

        System.out.println("호출!");
        log.info("오디오데이터 들어옴: " + audioData.length + "bytes");


//        Base64.Decoder decoder = Base64.getDecoder();
//        StringBuilder sb = new StringBuilder();
//
//        byte[] decoded = decoder.decode(audioData.split(",")[1]);
//        sb.append(new String(decoded, StandardCharsets.UTF_8));
//        log.info("오디오데이터 들어옴: " + sb);
//        FileOutputStream fos = new FileOutputStream("MyAudio.webm");
//        fos.write(decoded);
//        fos.close();
    }

//    @PostMapping("/stt")
//    public void startStt() throws LineUnavailableException, IOException {
//        sttService.startStt();
//
//    }

    @PostMapping("/api/v1/stt/stop")
    public ResponseEntity<Message<Void>> stopStt() {
        sttService.stopStt();
        return ResponseEntity.ok().body(Message.success());
    }

}
