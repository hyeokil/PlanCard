package com.ssafy.backend.domain.alarm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.backend.domain.alarm.entity.QAlarm.alarm;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<AlarmDto> findAlarmSliceByMemberId(Long memberId, Pageable pageable) {
        QMember fromMember = new QMember("fromMember");

        List<AlarmDto> alarms = queryFactory
                .select(Projections.bean(
                        AlarmDto.class,
                        alarm.id,
                        fromMember.id.as("fromMemberId"),
                        alarm.type,
                        alarm.content,
                        alarm.status
                ))
                .from(alarm)
                .leftJoin(alarm.fromMember, fromMember)
                .where(alarm.toMember.id.eq(memberId)
                        .and(alarm.status.ne(AlarmStatus.ACCEPTED))) // '수락된' 상태가 아닌 알람만 필터링)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(alarms, pageable, hasNext(alarms, pageable.getPageSize()));
    }

    private boolean hasNext(List<?> contents, int limit) {
        if (contents.size() > limit) {
            contents.remove(limit);
            return true;
        }
        return false;
    }
}
