package com.ssafy.backend.domain.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto {
    private Long friendId;
    private String name;
    private String email;
//    private String profileImage;

    // 온라인 여부

}
