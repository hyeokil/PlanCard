package com.ssafy.backend.global.component.jwt.service;

import com.ssafy.backend.global.component.jwt.JwtIssuer;
import com.ssafy.backend.global.component.jwt.JwtParser;
import com.ssafy.backend.global.component.jwt.JwtUtils;
import com.ssafy.backend.global.component.jwt.dto.SignUpTokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.repository.RefreshRepository;
import com.ssafy.backend.global.exception.GlobalError;
import com.ssafy.backend.global.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.ssafy.backend.global.component.jwt.JwtUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtUtils jwtUtils;
    private final JwtIssuer jwtIssuer;
    private final JwtParser jwtParser;
    private final RefreshRepository refreshRepository;


    @Override
    public TokenDto issueToken(@NonNull TokenMemberInfoDto tokenMemberInfoDto) {
        String accessToken = jwtIssuer.issueToken(
                tokenMemberInfoDto.toClaims(jwtUtils.getAccessTokenExpiredMin()), jwtUtils.getEncodedAccessKey()
        );

        String refreshToken = jwtIssuer.issueToken(
                tokenMemberInfoDto.toClaims(jwtUtils.getRefreshTokenExpiredMin()), jwtUtils.getEncodedRefreshKey()
        );

        try {
            // refreshToekn redis에 저장
            refreshRepository.save(tokenMemberInfoDto.getEmail(), refreshToken, jwtUtils.getRefreshTokenExpiredMin());
        }
        catch (Exception e) {
            throw new RuntimeException("Redis 연결에 실패했습니다.");
        }

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(jwtUtils.getAccessTokenExpiredMin())
                .grantType(BEARER_PREFIX)
                .build();
    }

    @Override
    public TokenMemberInfoDto parseAccessToken(@NonNull String accessToken) {
        Claims claims = jwtParser.parseToken(accessToken, jwtUtils.getEncodedAccessKey());
        if (claims == null) {
            return null;
        }
        return TokenMemberInfoDto.from(claims);
    }

    @Override
    public TokenDto reissueToken(@NonNull String memberEmail) {
        return null;
    }

    @Override
    public String issueSignUpToken(@NonNull String email, @NonNull String provider) {
        Claims claims = Jwts.claims();
        claims.put(KEY_ID, email);
        claims.put(KEY_PROVIDER, provider);

        return jwtIssuer.issueToken(claims, jwtUtils.getEncodedSignupKey());
    }

    @Override
    public SignUpTokenMemberInfoDto parseSignUpToken(@NonNull String signUpToken) {
        Claims claims = jwtParser.parseToken(signUpToken, jwtUtils.getEncodedSignupKey());

        if(!StringUtils.hasText(claims.get(KEY_PROVIDER, String.class))) {
            throw new TokenException(GlobalError.INVALID_TOKEN);
        }

        return SignUpTokenMemberInfoDto.from(claims);
    }
}
