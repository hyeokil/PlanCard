package com.ssafy.backend.domain.friend.service;

import com.ssafy.backend.global.common.dto.SliceResponse;
import org.springframework.data.domain.Pageable;

public interface FriendshipService {
    void accept(Long alarmId, Long ownerId, Long friendId);

    void refuse(Long alarmId);

    void requestFriendship(Long ownerId, Long friendId);

    SliceResponse getFriends(Long ownerId, Pageable pageable);
}
