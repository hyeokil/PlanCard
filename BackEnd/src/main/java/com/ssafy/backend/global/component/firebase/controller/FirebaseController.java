package com.ssafy.backend.global.component.firebase.controller;

import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.component.firebase.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/firebase")
public class FirebaseController {
    private final FirebaseService firebaseService;

    @PostMapping("/upload")
    public ResponseEntity<Message<String>> uploadFiles(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("nameFile") String nameFile) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.ok().body(Message.fail("0", "is empty"));
        }
        String url = firebaseService.uploadFiles(file, nameFile);
        return ResponseEntity.ok().body(Message.success(url));
    }
}
