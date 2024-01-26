package com.ssafy.backend.domain.member.dto;

import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class MemberLoginActiveDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String image;
    private String role;

    public static MemberLoginActiveDto from(TokenMemberInfoDto infoDto) {
        return MemberLoginActiveDto.builder()
                .id(infoDto.getId())
                .email(infoDto.getEmail())
                .name(infoDto.getName())
                .nickname(infoDto.getNickname())
                .image(infoDto.getImage())
                .role(infoDto.getRole())
                .build();
    }
}
