package com.ssafy.backend.domain.chat.repository;

import com.ssafy.backend.domain.chat.document.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, Long> {
    List<Chat> findByRoomId(Long roomId);
}
