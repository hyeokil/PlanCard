package com.ssafy.backend.test;

import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class TestConfig {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // 유저 데이터 집어넣기
        Member member1 = Member.builder()
                .email("test1@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("테스트1")
                .nickname("테스트 닉네임1")
                .role(MemberRole.USER)
                .build();

        Member member2 = Member.builder()
                .email("test2@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("테스트2")
                .nickname("테스트 닉네임2")
                .role(MemberRole.USER)
                .build();

        Member member3 = Member.builder()
                .email("test3@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("테스트3")
                .nickname("테스트 닉네임3")
                .role(MemberRole.USER)
                .build();

        Member member4 = Member.builder()
                .email("test4@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("테스트4")
                .nickname("테스트 닉네임4")
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);


    }
}
