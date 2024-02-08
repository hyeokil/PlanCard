package com.ssafy.backend.domain.friend.repository;

import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.friend.entity.Friendship;
import com.ssafy.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>, FriendshipRepositoryCustom {
    // 이미 친구 관계가 설정되어 있는지 확인하는 쿼리
    boolean existsByOwnerAndFriend(Member owner, Member friend);

    // 친구 요청을 이미 보냈는지 확인하는 쿼리 (알람 테이블에 존재하는지)
    boolean existsByFromMemberAndToMemberAndStatusAndType(Member fromMember, Member toMember, AlarmStatus status, AlarmType type);
}
