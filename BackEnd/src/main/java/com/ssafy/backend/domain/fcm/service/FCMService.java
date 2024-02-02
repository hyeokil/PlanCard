package com.ssafy.backend.domain.fcm.service;

public interface FCMService {

    void saveFCMToken(Long memberId, String token);

    void deleteFCMToken(Long memberId);

    void sendMessageTo(Long memberId, String title, String content);
}
