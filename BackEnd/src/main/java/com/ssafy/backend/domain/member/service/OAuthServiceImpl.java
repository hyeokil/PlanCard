package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberLoginResponseDto;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import com.ssafy.backend.global.component.oauth.OAuthCodeUrlProvider;
import com.ssafy.backend.global.component.oauth.OAuthMemberClient;
import com.ssafy.backend.global.component.oauth.vendor.enums.OAuthDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class OAuthServiceImpl implements OAuthService {

    private final OAuthCodeUrlProvider oAuthCodeUrlProvider;
    private final OAuthMemberClient oAuthMemberClient;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public String provideAuthCodeRequestUrl(OAuthDomain oauthDomain) {
        return oAuthCodeUrlProvider.provide(oauthDomain);
    }

    @Override
    public TokenMemberInfoDto login(OAuthDomain oauthDomain, String authCode) {
        Member oauthMember = oAuthMemberClient.fetch(oauthDomain, authCode);
        Member member = memberRepository.findByEmail(oauthMember.getEmail())
                .orElseGet(() -> memberRepository.save(oauthMember));

        return TokenMemberInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .image(member.getImage())
                .role(MemberRole.USER.toString())
                .build();
    }

}
