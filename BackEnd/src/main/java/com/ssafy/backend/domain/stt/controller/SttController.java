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
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SttController {
    private final SttService sttService;

    @MessageMapping("/api/v1/audio") //websocket에서 제공하는 vue에서 메시지 받을 컨트롤러
    public void SttHandler(String base64Audio) throws LineUnavailableException, IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        StringBuilder sb = new StringBuilder();

//        byte[] decoded = decoder.decode(base64Audio.split(",")[1]);
//        sb.append(new String(decoded, StandardCharsets.UTF_8));
//        log.info("오디오데이터 들어옴: " + sb);
//        String dummy = "data:audio/webm;base64,GkXfo59ChoEBQveBAULygQRC84EIQoKEd2VibUKHgQRChYECGFOAZwH/////////FUmpZpkq17GDD0JATYCGQ2hyb21lV0GGQ2hyb21lFlSua7+uvdeBAXPFh2kUz9yhnp6DgQKGhkFfT1BVU2Oik09wdXNIZWFkAQEAAIC7AAAAAADhjbWERzuAAJ+BAWJkgSAfQ7Z1Af/////////ngQCjRLWBAACA+4P9Qf0JfcWdkxOG5X3hjLyYhlGNVHUn9D8B1PHsvlJSJR5eLKmP8cdgrF1Fb66T/pGRlewx9DyJKsMJAblqTJwoUrH7AMrcGkGc0swDP38zlOd32hD5xpr7xxu0Xp4xkne97kRAslXuZYNFf9yZBfxe9hfR2BPS2WYgYQ7lwt/hngQWekkzgwv2NjWDokfcOTsCU6HNR2Tiw+Z5d61CS+Wg";
//        byte[] decoded = decoder.decode(dummy.split(",")[1]);
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
