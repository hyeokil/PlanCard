package com.ssafy.backend.domain.friend.service;

import com.ssafy.backend.domain.friend.dto.FriendshipDto;
import com.ssafy.backend.domain.friend.entity.Friendship;
import com.ssafy.backend.domain.friend.repository.FriendshipRepository;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import com.ssafy.backend.global.common.dto.SliceResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    /**
     * 친구추가 수락 처리
     * @param ownerId 수락한 회원 id
     * @param friendId 친구추가 요청한 회원 id
     */
    @Override
    public void accept(Long ownerId, Long friendId) {
        Member owner = memberRepository.findById(ownerId).orElseThrow();
        Member target = memberRepository.findById(friendId).orElseThrow();

        Friendship ownerFriendship = Friendship.builder()
                .owner(owner)
                .friend(target)
                .build();

        Friendship targetFriendship = Friendship.builder()
                .owner(target)
                .friend(owner)
                .build();

        friendshipRepository.save(ownerFriendship);
        friendshipRepository.save(targetFriendship);
    }

    @Override
    public SliceResponse getFriends(Long ownerId, Pageable pageable) {
        Slice<FriendshipDto> friends = friendshipRepository.findFriends(ownerId, pageable);
        return SliceResponse.of(friends);
    }
}
