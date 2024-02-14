package com.ssafy.backend.domain.stt.service;

import org.json.simple.parser.ParseException;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public interface SttService {

    String getAccessToken() throws IOException;

    void startTranscribe(Long planId) throws IOException, LineUnavailableException, ParseException;

//    Long sttToCard(String text);

    void stopStt(Long planId);

    void processAudioData(byte[] audioData) throws IOException;

}
