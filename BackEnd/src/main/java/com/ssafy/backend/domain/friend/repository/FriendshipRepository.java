package com.ssafy.backend.domain.friend.repository;

import com.ssafy.backend.domain.friend.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>, FriendshipRepositoryCustom {
//    친구 목록을 Querydsl 대신 @Query로 엮을 수 있지 않을까?...
}
