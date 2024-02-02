package com.ssafy.backend.domain.member.controller;

import com.ssafy.backend.domain.member.dto.MemberLoginResponseDto;
import com.ssafy.backend.domain.member.service.OAuthService;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import com.ssafy.backend.global.component.oauth.vendor.enums.OAuthDomain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {
    private final OAuthService oAuthService;
    private final JwtService jwtService;

    @GetMapping("/{oAuthDomain}")
    public ResponseEntity<Message<String>> provideAuthCodeRequestUrl(@PathVariable("oAuthDomain") OAuthDomain oAuthDomain) {
        String redirectUrl = oAuthService.provideAuthCodeRequestUrl(oAuthDomain);
//        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().body(Message.success(redirectUrl));
    }

    @GetMapping("/{oAuthDomain}/login")
    public ResponseEntity<Message<MemberLoginResponseDto>> login(@PathVariable("oAuthDomain") OAuthDomain oAuthDomain,
                                                                 @RequestParam("code") String authCode,
                                                                 HttpServletResponse response) {
        TokenMemberInfoDto tokenMemberInfoDto = oAuthService.login(oAuthDomain, authCode);
        TokenDto tokenDto = jwtService.issueToken(tokenMemberInfoDto);
        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
                .memberInfo(tokenMemberInfoDto)
                .token(tokenDto)
                .build();
        // JWT 토큰을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600); // 60분(3600초)으로 설정
        response.addCookie(accessTokenCookie);
        return ResponseEntity.ok().body(Message.success(loginResponseDto));
    }
}
