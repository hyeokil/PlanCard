package com.ssafy.backend.domain.friend.service;

import com.ssafy.backend.domain.alarm.entity.Alarm;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmStatus;
import com.ssafy.backend.domain.alarm.entity.enums.AlarmType;
import com.ssafy.backend.domain.alarm.repository.AlarmRepository;
import com.ssafy.backend.domain.fcm.service.FCMService;
import com.ssafy.backend.domain.friend.dto.FriendshipDto;
import com.ssafy.backend.domain.friend.entity.Friendship;
import com.ssafy.backend.domain.friend.repository.FriendshipRepository;
import com.ssafy.backend.domain.member.entity.Member;
import com.ssafy.backend.domain.member.exception.MemberError;
import com.ssafy.backend.domain.member.exception.MemberException;
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
    private final AlarmRepository alarmRepository;
    private final FCMService fcmService;

    /**
     * 친구추가 수락 처리
     * @param ownerId 수락한 회원 id
     * @param friendId 친구추가 요청한 회원 id
     */
    @Override
    public void accept(Long alarmId, Long ownerId, Long friendId) {
        Member owner = memberRepository.findById(ownerId).orElseThrow(() -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        Member target = memberRepository.findById(friendId).orElseThrow(() -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        Friendship ownerFriendship = Friendship.builder()
                .owner(owner)
                .friend(target)
                .build();

        Friendship targetFriendship = Friendship.builder()
                .owner(target)
                .friend(owner)
                .build();

        // alarm 찾고 
        // 해당 alarm 상태 변경
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow();
        alarm.accept();

        friendshipRepository.save(ownerFriendship);
        friendshipRepository.save(targetFriendship);
    }

    @Override
    public void refuse(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow();
        alarmRepository.delete(alarm);
    }

    // 친구추가 요청
    @Override
    public void requestFriendship(Long ownerId, Long friendId) {
        Member owner = memberRepository.findById(ownerId).orElseThrow(() -> new MemberException(MemberError.NOT_FOUND_MEMBER));
        Member target = memberRepository.findById(friendId).orElseThrow(() -> new MemberException(MemberError.NOT_FOUND_MEMBER));

        // 친구요청 부분으로 이동
        Alarm alarm = Alarm.builder().fromMember(owner)
                .toMember(target)
                .title(AlarmType.FRIEND.getTitle())
                .content(owner.getNickname() + AlarmType.FRIEND.getContent())
                .type(AlarmType.FRIEND)
                .status(AlarmStatus.UNREAD)
                .build();

        alarmRepository.save(alarm);

        fcmService.sendMessageTo(target.getId(), alarm.getTitle(), alarm.getContent());
    }

    @Override
    public SliceResponse getFriends(Long ownerId, Pageable pageable) {
        Slice<FriendshipDto> friends = friendshipRepository.findFriends(ownerId, pageable);
        return SliceResponse.of(friends);
    }
}
