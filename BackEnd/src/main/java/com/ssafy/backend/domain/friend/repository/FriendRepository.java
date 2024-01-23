package com.ssafy.backend.domain.friend.repository;

import com.ssafy.backend.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

}
