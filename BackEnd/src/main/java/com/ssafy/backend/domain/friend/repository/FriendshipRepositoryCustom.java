package com.ssafy.backend.domain.friend.repository;

import com.ssafy.backend.domain.friend.dto.FriendshipDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FriendshipRepositoryCustom {
    Slice<FriendshipDto> findFriends(Long ownerId, Pageable pageable);
}
