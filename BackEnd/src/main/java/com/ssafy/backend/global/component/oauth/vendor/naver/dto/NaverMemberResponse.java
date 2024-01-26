package com.ssafy.backend.global.component.oauth.vendor.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.global.component.oauth.vendor.enums.OAuthDomain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(SnakeCaseStrategy.class)
public class NaverMemberResponse {
    private String resultCode;
    private String message;
    private Response response;


    public Member toDomain() {
        return Member.builder()
                .email(response.getEmail())
                .name(response.getName())
                .nickname(response.getNickname())
                .image(response.getProfileImage())
                .role(MemberRole.USER)
                .oAuthDomain(OAuthDomain.NAVER)
                .build();
    }

    @Getter
    @Setter
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Response {
       private String id;
       private String nickname;
       private String name;
       private String email;
       private String gender;
       private String age;
       private String birthday;
       private String profileImage;
       private String birthyear;
       private String mobile;
    }

}
