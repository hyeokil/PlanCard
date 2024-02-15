package com.ssafy.backend.global.component.websocket;

import com.ssafy.backend.domain.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.nio.ByteBuffer;

@Slf4j
@RequiredArgsConstructor
@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private final SttService sttService;

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        log.info("Received binary message of size: {}", payload.remaining());
        log.info("payload 확인하기: {}", payload);
        byte[] audioData = new byte[payload.remaining()];
//        payload.get(audioData);


        sttService.processAudioData(audioData);
    }



}
