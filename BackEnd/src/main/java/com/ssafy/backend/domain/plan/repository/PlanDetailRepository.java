package com.ssafy.backend.domain.plan.repository;

import com.ssafy.backend.domain.plan.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail,Long> {
    // 기본적인 CRUD는 JPA에서 제공
    List<PlanDetail> findByPlanId(Long planId);

}
