package com.ssafy.backend.domain.stt.controller;

import com.ssafy.backend.domain.stt.service.SttService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SttController {

    private final SttService sttService;

    @MessageMapping("/ws-audio") //websocket에서 제공하는 vue에서 메시지 받을 컨트롤러
//    @SendTo("/send/audio") //다시 vue로 반환위한 url
    public void SttHandler(@Payload byte[] audioData) throws LineUnavailableException, IOException {
        System.out.println("호출!");
        log.info("오디오데이터 들어옴: " + audioData.length + "bytes");
    }

//    @PostMapping("/stt")
//    public void startStt() throws LineUnavailableException, IOException {
//        sttService.startStt();
//
//    }

    @PostMapping("/stt/stop")
    public ResponseEntity<Message<Void>> stopStt() {
        sttService.stopStt();
        return ResponseEntity.ok().body(Message.success());
    }

}
