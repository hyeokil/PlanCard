package com.ssafy.backend.domain.plandetail.repository;

import com.ssafy.backend.domain.plandetail.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail,Long> {
    // 기본적인 CRUD는 JPA에서 제공
}
