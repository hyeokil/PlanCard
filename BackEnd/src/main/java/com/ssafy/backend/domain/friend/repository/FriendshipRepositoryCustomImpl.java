package com.ssafy.backend.domain.friend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.backend.domain.friend.dto.FriendshipDto;
import com.ssafy.backend.domain.friend.entity.QFriendship;
import com.ssafy.backend.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssafy.backend.domain.friend.entity.QFriendship.*;
import static com.ssafy.backend.domain.member.entity.QMember.*;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryCustomImpl implements FriendshipRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<FriendshipDto> findFriends(Long ownerId, Pageable pageable) {

        // TODO profileImage 추가
        List<FriendshipDto> friendShip = queryFactory
                .select(Projections.bean(FriendshipDto.class, member.name, member.email, member.id.as("friendId")))
                .from(friendship)
                .join(friendship.friend, member)
                .where(friendship.owner.id.eq(ownerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(friendShip, pageable, hasNext(friendShip, pageable.getPageSize()));
    }

    private boolean hasNext(List<?> contents, int limit) {
        if (contents.size() > limit) {
            contents.remove(limit);
            return true;
        }
        return false;
    }
}
