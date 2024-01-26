package com.ssafy.backend.domain.planmember.repository;

import com.ssafy.backend.domain.plan.entity.Plan;
import com.ssafy.backend.domain.planmember.entity.PlanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
    // 기본적인 CRUD는 JPA에서 제공

}
