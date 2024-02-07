package com.ssafy.backend.global.config;

import com.ssafy.backend.domain.stt.controller.SttSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class SttSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SttSocketHandler sttSocketHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 WebSocket 연결을 수락할 엔드포인트를 설정합니다.
        registry.addEndpoint("/send")
                .setAllowedOriginPatterns("http://localhost:8080")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지를 구독할 때 사용할 메시지 브로커를 구성합니다.
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/send"); // 클라이언트에게 메시지를 보낼 때 사용할 프리픽스를 설정합니다.
    }
}


