package com.ssafy.backend.domain.plan.service;

import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.plan.repository.PlanRepository;
import com.ssafy.backend.domain.plan.entity.PlanMember;
import com.ssafy.backend.domain.plan.repository.PlanMemberRepository;
import com.ssafy.backend.domain.plan.service.PlanMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class PlanMemberServiceImpl implements PlanMemberService {
    private final PlanRepository planRepository;
    private final PlanMemberRepository planMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public void acceptPlan(Long planId, Long memberId) {
        Plan plan = planRepository.findById(planId).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        PlanMember planMember = PlanMember.builder()
                .plan(plan)
                .member(member)
                .build();
        planMemberRepository.save(planMember);
    }
}
