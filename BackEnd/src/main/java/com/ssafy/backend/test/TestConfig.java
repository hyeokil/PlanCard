package com.ssafy.backend.test;

import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.planmember.entity.PlanMember;
import com.ssafy.backend.domain.planmember.repository.PlanMemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@RequiredArgsConstructor
@Configuration
public class TestConfig {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;

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

        Plan plan1 = Plan.builder()
                .name("테스트 여행계획 1")
                .startDate(LocalDate.of(2024, 2, 10)) // 예시 날짜
                .endDate(LocalDate.of(2024, 2, 27)) // 예시 날짜
                .build();

        Plan plan2 = Plan.builder()
                .name("테스트 여행계획 2")
                .startDate(LocalDate.of(2024, 3, 1)) // 예시 날짜
                .endDate(LocalDate.of(2024, 3, 5)) // 예시 날짜
                .build();

        planRepository.save(plan1);
        planRepository.save(plan2);


        PlanMember planMember1 = PlanMember.builder()
                .plan(plan1)
                .member(member1)
                .build();

        PlanMember planMember2 = PlanMember.builder()
                .plan(plan1)
                .member(member2)
                .build();

        PlanMember planMember3 = PlanMember.builder()
                .plan(plan2)
                .member(member3)
                .build();

        planMemberRepository.save(planMember1);
        planMemberRepository.save(planMember2);
        planMemberRepository.save(planMember3);
    }
}
