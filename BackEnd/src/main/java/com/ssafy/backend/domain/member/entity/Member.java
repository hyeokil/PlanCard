package com.ssafy.backend.domain.member.entity;

import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.global.common.entity.BaseEntity;
import com.ssafy.backend.global.component.oauth.vendor.enums.OAuthDomain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String image;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private OAuthDomain oAuthDomain;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateImage(String image) { this.image = image; }

}
