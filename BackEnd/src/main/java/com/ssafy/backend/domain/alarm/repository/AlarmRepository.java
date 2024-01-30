package com.ssafy.backend.domain.alarm.repository;

import com.ssafy.backend.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
}
