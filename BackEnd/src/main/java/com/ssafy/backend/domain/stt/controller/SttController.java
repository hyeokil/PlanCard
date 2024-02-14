package com.ssafy.backend.domain.stt.controller;

import com.ssafy.backend.domain.stt.service.SttService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/stt")
@RequiredArgsConstructor
public class SttController {

    private final SttService sttService;

    @PostMapping("/{planId}/start")
    public ResponseEntity<Message<Void>> startTranscribe(@PathVariable Long planId) throws LineUnavailableException, IOException, ParseException {
        sttService.startTranscribe(planId);
        return ResponseEntity.ok().body(Message.success());
    }

    @PostMapping("/{planId}/stop")
    public ResponseEntity<Message<Void>> stopStt(@PathVariable Long planId) {
        sttService.stopStt(planId);
        return ResponseEntity.ok().body(Message.success());
    }

    @MessageMapping("/audio")
    public void audioRequest(@Payload byte[] audioData) throws IOException {
        sttService.processAudioData(audioData);
        log.info("audio 데이터 들어옴: " + audioData.length + "bytes");
    }

}
