package com.bookbook.booklink.chat.single.repository;

import com.bookbook.booklink.chat.single.model.OneToOneChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OneToOneChatsRepository extends JpaRepository<OneToOneChats, UUID> {
    // 두 유저간의 채팅방이 이미 있는지 확인
    Optional<OneToOneChats> findByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);

    Optional<OneToOneChats> findByUser2IdAndUser1Id(UUID user2Id, UUID user1Id);
}
    