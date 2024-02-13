package com.ssafy.backend.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class SttSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 WebSocket 연결을 수락할 엔드포인트를 설정합니다.
        registry.addEndpoint("/send")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // webSocket을 지원하지 않는 브라우저 경우에 대해서 런타임에 필요할 때 대체하기 위함
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        log.info("메시지 브로커 인식");
        // 클라이언트가 메시지를 구독할 때 사용할 메시지 브로커를 구성합니다.
        registry.setApplicationDestinationPrefixes("/app") //브로커 메시지 publish 경로
                .enableSimpleBroker("/audio"); //subscribe 경로 설정
    }
    
    //사용자 지정 웹소켓 핸들러 추가
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(handler -> (WebSocketHandler) new BinaryWebSocketHandler(handler));
    }

    
    //바이너리 데이터 받을 수 있도록 메시지 컨버터 추가
    private static class BinaryWebSocketHandler extends WebSocketHandlerDecorator {
        public BinaryWebSocketHandler(WebSocketHandler delegate) {
            super(delegate);
        }

        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            // Binary message handling logic here
            if (message instanceof BinaryMessage) {
                byte[] payload = ((BinaryMessage) message).getPayload().array();
                // Process the binary payload
                log.info("Received binary data: {} bytes", payload.length);
            } else {
                // Delegate handling to the original handler for non-binary messages
                super.handleMessage(session, message);
            }
        }
    }
}


