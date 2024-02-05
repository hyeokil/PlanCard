package com.ssafy.backend.domain.chat.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    private String id;
    @Field("room_id")
    private Long roomId;
    @Field("member_id")
    private Long memberId;
    @Field("sender_name")
    private String nickname;
    @Field("image")
    private String image;
    @Field("content")
    private String message;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime sendTime;
}
