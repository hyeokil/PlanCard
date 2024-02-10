package com.ssafy.backend.domain.stt.controller;

import com.ssafy.backend.domain.stt.service.SttService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/stt")
    public void startStt() throws LineUnavailableException, IOException {
        sttService.startStt();
    }

    @PostMapping("/stt/stop")
    public ResponseEntity<Message<Void>> stopStt() {
        sttService.stopStt();
        return ResponseEntity.ok().body(Message.success());
    }

}
