package com.bookbook.booklink.chat_service.single.repository;

import com.bookbook.booklink.chat_service.single.model.SingleChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SingleChatsRepository extends JpaRepository<SingleChats, UUID> {
    // 두 유저간의 채팅방이 이미 있는지 확인
    Optional<SingleChats> findByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);
}
    