package com.ssafy.backend.domain.chat.controller;

import com.ssafy.backend.domain.chat.document.Chat;
import com.ssafy.backend.domain.chat.dto.ChatMessageDto;
import com.ssafy.backend.domain.chat.service.ChatService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/api/v1/chat/{roomId}")
    public void send(Principal principal, ChatMessageDto chatMessageDto, @DestinationVariable Long roomId) {
        chatService.sendMessage(chatMessageDto, Long.valueOf(principal.getName()), roomId);
    }

    @GetMapping("/api/v1/history/{roomId}")
    public ResponseEntity<Message<List<Chat>>> getChatHistory(@PathVariable Long roomId) {
        List<Chat> chatHistory = chatService.getChatHistory(roomId);
        return ResponseEntity.ok().body(Message.success(chatHistory));
    }
}
