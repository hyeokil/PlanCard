package com.ssafy.backend.global.component.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.*;

import static com.ssafy.backend.global.component.jwt.JwtUtils.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignUpTokenMemberInfoDto {
    private String id;
    private String provider;

    public static SignUpTokenMemberInfoDto from(Claims claims) {
        return SignUpTokenMemberInfoDto.builder()
                .id(claims.get(KEY_ID, String.class))
                .provider(claims.get(KEY_PROVIDER, String.class))
                .build();
    }

}
