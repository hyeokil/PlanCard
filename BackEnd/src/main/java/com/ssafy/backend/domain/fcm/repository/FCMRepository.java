package com.ssafy.backend.domain.fcm.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FCMRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "fcmToken::";

    public void save(Long memberId, String token) {
        redisTemplate.opsForValue().set(KEY_PREFIX + memberId, token, Duration.ofDays(60)); // FCM Token은 2개월 유효 권장
    }

    public void delete(Long memberId) {
        redisTemplate.delete(KEY_PREFIX + memberId);
    }

    public Optional<String> find(Long memberId) {
        String token = redisTemplate.opsForValue().get(KEY_PREFIX + memberId);
        return Optional.ofNullable(token);
    }
}
