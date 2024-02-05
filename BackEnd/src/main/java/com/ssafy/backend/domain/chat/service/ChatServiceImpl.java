package com.ssafy.backend.domain.chat.service;

import com.ssafy.backend.domain.chat.document.Chat;
import com.ssafy.backend.domain.chat.dto.ChatDto;
import com.ssafy.backend.domain.chat.dto.ChatMessageDto;
import com.ssafy.backend.domain.chat.repository.ChatRepository;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;
    private final ChatRepository chatRepository;

    @Override
    public void sendMessage(ChatMessageDto messageDto, Long memberId, Long roomId) {
        log.info("{}", memberId);
        if (!Objects.equals(memberId, messageDto.getMemberId())) {
            throw new MemberException(MemberError.NOT_FOUND_MEMBER);
        }

        Chat chat = Chat.builder()
                .roomId(roomId)
                .memberId(memberId)
                .nickname(messageDto.getNickname())
                .image(messageDto.getImage())
                .message(messageDto.getMessage())
                .sendTime(LocalDateTime.now())
                .build();

        chatRepository.save(chat);

        ChatDto chatDto = ChatDto.builder()
                .memberId(memberId)
                .nickname(messageDto.getNickname())
                .image(messageDto.getImage())
                .message(messageDto.getMessage())
                .sendTime(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(topicExchange.getName(), "room." + roomId, chatDto);
    }

    @Override
    public List<Chat> getChatHistory(Long roomId) {
        return chatRepository.findByRoomId(roomId);
    }


}
