package com.ssafy.backend.domain.friend.repository;

import com.ssafy.backend.domain.friend.entity.Friendship;
import com.ssafy.backend.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>, FriendshipRepositoryCustom {
//    Querydsl 대신 @Query로 엮을 수 있지 않을까?...
//    @Query("select f from Friendship fs join fs.friend f where fs.owner = :ownerId")
//    Slice<Member> findFriendsByOwnerId(Long ownerId, Pageable pageable);
}
