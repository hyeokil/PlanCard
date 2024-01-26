package com.ssafy.backend.domain.friend.service;

import com.ssafy.backend.domain.friend.dto.FriendshipDto;
import com.ssafy.backend.global.common.dto.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FriendshipService {
    void accept(Long ownerId, Long friendId);

    SliceResponse getFriends(Long ownerId, Pageable pageable);
}
