package com.ssafy.backend.domain.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ssafy.backend.domain.fcm.repository.FCMRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {
    private final FCMRepository fcmRepository;

    @Override
    public void saveFCMToken(Long memberId, String token) {
        // redis에 id : FCM Token 쌍으로 저장
        fcmRepository.save(memberId, token);
    }

    @Override
    public void deleteFCMToken(Long memberId) {
        // redis에서 key가 id인 값 찾아서 삭제
        fcmRepository.delete(memberId);
    }

    @Override
    public void sendMessageTo(Long memberId, String body) {
        // token 찾고
        Optional<String> fcmToken = fcmRepository.find(memberId);

        if (fcmToken.isEmpty()) {
            return;
        }

        String token = fcmToken.get();

        // makeMessage 로 리팩토링
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setBody(body)
                        .build())
                .build();

        // firebase에 비동기 전송
        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
