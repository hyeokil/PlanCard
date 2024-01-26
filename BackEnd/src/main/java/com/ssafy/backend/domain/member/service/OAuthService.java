package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberLoginResponseDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.oauth.vendor.enums.OAuthDomain;

public interface OAuthService {
    String provideAuthCodeRequestUrl(OAuthDomain oauthDomain);

    TokenMemberInfoDto login(OAuthDomain oauthDomain, String authCode);
}
