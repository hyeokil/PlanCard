package com.ssafy.backend.domain.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendshipRequestDto {
    private Long ownerId;
    private Long friendId;
}
