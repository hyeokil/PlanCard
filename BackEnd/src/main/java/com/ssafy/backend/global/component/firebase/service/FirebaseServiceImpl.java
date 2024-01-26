package com.ssafy.backend.global.component.firebase.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    @Override
    public String uploadFiles(MultipartFile file, String nameFile) throws IOException {

        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

        // 파일 업로드
        Blob blob = bucket.create(nameFile, file.getBytes(), file.getContentType());

        // 파일에 대한 공개 읽기 권한 부여
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        // 파일의 공개 URL 반환
        return String.format("https://storage.googleapis.com/%s/%s", firebaseBucket, nameFile);

    }
}
