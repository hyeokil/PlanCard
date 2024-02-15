package com.ssafy.backend.domain.stt.controller;

import com.ssafy.backend.domain.stt.service.SttService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/stt")
@CrossOrigin("http://localhost:8080")
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
    public void audioRequest(@Payload byte[] audioData) throws IOException, InterruptedException {
        log.info("audio 데이터 들어옴: " + audioData.length + "bytes");
    }

//    @PostMapping("/upload")
//    public void audioBlobRequest(@RequestParam("file") MultipartFile file) throws UnsupportedAudioFileException, IOException, InterruptedException {
//        sttService.processAudioData(file);
//        log.info("wav 데이터 받음 {}", file);
//    }

//    @MessageMapping("/audio")
//    public void audioRequest(@Payload MultipartFile file) throws IOException, UnsupportedAudioFileException, InterruptedException {
//        sttService.processAudioData(file);
//
//    }

}
