package com.ssafy.backend.domain.friend.service;

import com.ssafy.backend.domain.friend.entity.Friend;
import com.ssafy.backend.domain.friend.repository.FriendRepository;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
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

        Friend ownerFriendship = Friend.builder()
                .owner(owner)
                .friend(target)
                .build();

        Friend targetFriendship = Friend.builder()
                .owner(target)
                .friend(owner)
                .build();

        friendRepository.save(ownerFriendship);
        friendRepository.save(targetFriendship);
    }
}
