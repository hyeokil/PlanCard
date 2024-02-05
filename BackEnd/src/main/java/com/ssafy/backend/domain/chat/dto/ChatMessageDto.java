package com.ssafy.backend.domain.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDto {
    private Long memberId;
    private String nickname;
    private String image;
    private String message;
}
