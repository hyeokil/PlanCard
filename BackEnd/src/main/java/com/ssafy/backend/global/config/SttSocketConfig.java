package com.ssafy.backend.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class SttSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 WebSocket 연결을 수락할 엔드포인트를 설정합니다.
        registry.addEndpoint("/send")
//                .setAllowedOriginPatterns("*")
                .withSockJS(); // webSocket을 지원하지 않는 브라우저 경우에 대해서 런타임에 필요할 때 대체하기 위함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("메시지 브로커 인식");
        // 클라이언트가 메시지를 구독할 때 사용할 메시지 브로커를 구성합니다.
        registry.setApplicationDestinationPrefixes("/app") //브로커 메시지 publish 경로
                .enableSimpleBroker("/audio"); //subscribe 경로 설정
    }
}


