package com.ssafy.backend.domain.stt.service;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public interface SttService {
    void startStt() throws IOException, LineUnavailableException;

    void stopStt();
}
