package com.ssafy.backend.domain.chat.service;

import com.ssafy.backend.domain.chat.document.Chat;
import com.ssafy.backend.domain.chat.dto.ChatMessageDto;

import java.util.List;

public interface ChatService {
    void sendMessage(ChatMessageDto messageDto, Long memberId, Long roomId);

    List<Chat> getChatHistory(Long roomId);
}
