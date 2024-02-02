package com.ssafy.backend.domain.alarm.entity;

import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private AlarmStatus status;

    // status 관련 메서드들
    public void markAsRead() {
        if(this.status == AlarmStatus.UNREAD) {
            this.status = AlarmStatus.READ;
        }
    }
    public void accept() {
        this.status = AlarmStatus.ACCEPT;
    }

}
