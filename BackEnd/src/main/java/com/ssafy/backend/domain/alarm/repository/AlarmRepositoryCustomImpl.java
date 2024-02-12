package com.ssafy.backend.domain.alarm.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.backend.domain.alarm.dto.AlarmDto;
import com.ssafy.backend.domain.alarm.entity.QAlarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.backend.domain.alarm.entity.QAlarm.alarm;
import static com.ssafy.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<AlarmDto> findAlarmSliceByMemberId(Long memberId, Pageable pageable) {
        List<AlarmDto> alarms = queryFactory.select(
                        Projections.bean(
                                AlarmDto.class,
                                alarm.id.as("alarmId"),
                                alarm.fromMember.id.as("fromMemberId"),
                                alarm.toMember.id.as("toMemberId"),
                                alarm.type,
                                alarm.content,
                                alarm.status
                        ))
                .from(alarm)
                .where(alarm.toMember.id.eq(memberId)
                        .and(alarm.status.ne(AlarmStatus.ACCEPT)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(alarms, pageable, hasNext(alarms, pageable.getPageSize()));
    }

    @Override
    public List<AlarmDto> findAlarmsAfterId(Long memberId, Long lastAlarmId, int limit) {
        QAlarm alarm = QAlarm.alarm;

        BooleanExpression wherePredicate = alarm.toMember.id.eq(memberId)
                .and(alarm.status.ne(AlarmStatus.ACCEPT));

        if (lastAlarmId != null) {
            wherePredicate = wherePredicate.and(alarm.id.lt(lastAlarmId));
        }

        return queryFactory.select(
                        Projections.bean(
                                AlarmDto.class,
                                alarm.id.as("alarmId"),
//                                alarm.fromMember.id.as("fromMemberId"),
//                                alarm.fromMember.nickname.as("fromMemberNickname"),
                                member.id.as("fromMemberId"),
                                member.nickname.as("fromMemberNickname"),
                                alarm.toMember.id.as("toMemberId"),
                                alarm.type,
                                alarm.content,
                                alarm.status,
                                alarm.url
                        ))
                .from(alarm)
                .join(alarm.fromMember, member)
                .where(wherePredicate)
                .orderBy(alarm.id.desc())
                .limit(limit)
                .fetch();
    }

    private boolean hasNext(List<?> contents, int limit) {
        if (contents.size() > limit) {
            contents.remove(limit);
            return true;
        }
        return false;
    }
}
