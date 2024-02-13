package com.ssafy.backend.test;

import com.ssafy.backend.domain.alarm.entity.Alarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.alarm.repository.AlarmRepository;
import com.ssafy.backend.domain.friend.entity.Friendship;
import com.ssafy.backend.domain.friend.repository.FriendshipRepository;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.entity.enums.MemberRole;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.domain.place.service.PlaceService;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.plan.entity.PlanMember;
import com.ssafy.backend.domain.plan.repository.PlanMemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

@RequiredArgsConstructor
@Configuration
public class TestConfig {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;
    private final AlarmRepository alarmRepository;
    private final FriendshipRepository friendshipRepository;

    @PostConstruct
    public void init() throws IOException {
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

        Member member5 = Member.builder()
                .email("tlsehdrms95@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("신동근")
                .nickname("동근 닉네임")
                .role(MemberRole.USER)
                .build();

        Member member6 = Member.builder()
                .email("qkrwjddls96@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("박정인")
                .nickname("정인 닉네임")
                .role(MemberRole.USER)
                .build();

        Member member7 = Member.builder()
                .email("강지수96@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("강지수")
                .nickname("지수 닉네임")
                .role(MemberRole.USER)
                .build();

        Member member8 = Member.builder()
                .email("rlagurdlf97@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("김혁일")
                .nickname("혁일 닉네임")
                .role(MemberRole.USER)
                .build();

        Member member9 = Member.builder()
                .email("rlawogns98@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("김재훈")
                .nickname("재훈 닉네임")
                .role(MemberRole.USER)
                .build();

        Member member10 = Member.builder()
                .email("dltpdms99@ssafy.com")
                .password(passwordEncoder.encode("1234"))
                .name("이세은")
                .nickname("세은 닉네임")
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // 싸피 인원들 저장
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);
        memberRepository.save(member9);
        memberRepository.save(member10);

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

        Alarm alarm1 = Alarm.builder()
                .fromMember(member2)
                .toMember(member1)
                .type(AlarmType.CONFERENCE)
                .content(member2.getNickname() + " 님이 화상 회의 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .url("http://localhost:5173/meeting/view/1")
                .build();

        Alarm alarm2 = Alarm.builder()
                .fromMember(member2)
                .toMember(member1)
                .type(AlarmType.FRIEND)
                .content(member2.getNickname() + " 님이 친구 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .build();

        Alarm alarm3 = Alarm.builder()
                .fromMember(member3)
                .toMember(member1)
                .type(AlarmType.CONFERENCE)
                .content(member3.getNickname() + " 님이 화상 회의 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .build();

        Alarm alarm4 = Alarm.builder()
                .fromMember(member3)
                .toMember(member1)
                .type(AlarmType.FRIEND)
                .content(member3.getNickname() + " 님이 친구 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .build();

        Alarm alarm5 = Alarm.builder()
                .fromMember(member4)
                .toMember(member1)
                .type(AlarmType.FRIEND)
                .content(member4.getNickname() + " 님이 친구 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .build();

        Alarm alarm6 = Alarm.builder()
                .fromMember(member4)
                .toMember(member1)
                .type(AlarmType.CONFERENCE)
                .content(member4.getNickname() + " 님이 화상 회의 요청을 보냈습니다.")
                .status(AlarmStatus.UNREAD)
                .build();

        alarmRepository.save(alarm1);
        alarmRepository.save(alarm2);
        alarmRepository.save(alarm3);
        alarmRepository.save(alarm4);
        alarmRepository.save(alarm5);
        alarmRepository.save(alarm6);

        Friendship friendship1 = Friendship.builder()
                .owner(member1)
                .friend(member5)
                .build();

        Friendship friendship2 = Friendship.builder()
                .owner(member1)
                .friend(member6)
                .build();

        Friendship friendship3 = Friendship.builder()
                .owner(member1)
                .friend(member7)
                .build();

        Friendship friendship4 = Friendship.builder()
                .owner(member1)
                .friend(member8)
                .build();

        Friendship friendship5 = Friendship.builder()
                .owner(member1)
                .friend(member9)
                .build();

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
        friendshipRepository.save(friendship3);
        friendshipRepository.save(friendship4);
        friendshipRepository.save(friendship5);
    }


}
