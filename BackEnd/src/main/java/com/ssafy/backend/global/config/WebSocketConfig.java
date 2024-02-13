<<<<<<< HEAD
//package com.ssafy.backend.global.config;
//
//import com.ssafy.backend.global.component.websocket.RabbitMqProps;
//import com.ssafy.backend.global.component.websocket.WebSocketAuthenticationInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.ChannelRegistration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@RequiredArgsConstructor
//@EnableWebSocketMessageBroker
//@Configuration
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    private final RabbitMqProps rabbitMqProps;
//    private final WebSocketAuthenticationInterceptor authenticationInterceptor;
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws")
//                .setAllowedOrigins("*");
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setApplicationDestinationPrefixes("/pub") // pub으로 시작되는 메시지는 브로커로 보내짐, 메시지를 publish하는 경로
//                .setUserDestinationPrefix("/users") // 특정 사용자에게 메시지 전송시 사용할 주소
//                .enableStompBrokerRelay("/queue", "/topic", "/exchange","/amq/queue") // subscribe 경로를 설정, 메시지 브로커에서 지원하는 접두사들
//                .setRelayHost("localhost")
//                .setVirtualHost("/")
//                .setRelayPort(61613) // RabbitMQ STOMP 기본 포트
//                .setSystemLogin(rabbitMqProps.getUsername())
//                .setSystemPasscode(rabbitMqProps.getPassword())
//                .setClientLogin(rabbitMqProps.getUsername())
//                .setClientPasscode(rabbitMqProps.getPassword());
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(authenticationInterceptor);
//    }
//}
=======
package com.ssafy.backend.global.config;

import com.ssafy.backend.global.component.websocket.RabbitMqProps;
import com.ssafy.backend.global.component.websocket.WebSocketAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final RabbitMqProps rabbitMqProps;
    private final WebSocketAuthenticationInterceptor authenticationInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub") // pub으로 시작되는 메시지는 브로커로 보내짐, 메시지를 publish하는 경로
                .setUserDestinationPrefix("/users") // 특정 사용자에게 메시지 전송시 사용할 주소
                .enableStompBrokerRelay("/queue", "/topic", "/exchange","/amq/queue") // subscribe 경로를 설정, 메시지 브로커에서 지원하는 접두사들
                .setRelayHost(rabbitMqProps.getHost())
                .setVirtualHost("/")
                .setRelayPort(61613) // RabbitMQ STOMP 기본 포트
                .setSystemLogin(rabbitMqProps.getUsername())
                .setSystemPasscode(rabbitMqProps.getPassword())
                .setClientLogin(rabbitMqProps.getUsername())
                .setClientPasscode(rabbitMqProps.getPassword());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authenticationInterceptor);
    }
}
>>>>>>> 2333dcb6953b9eeeca6f6e3e099218c308521f0c
