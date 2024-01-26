package com.ssafy.backend.global.component.firebase.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseService {
    String uploadFiles(MultipartFile file, String nameFile) throws IOException;
}
