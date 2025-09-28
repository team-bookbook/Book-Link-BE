package com.bookbook.booklink.chat.chat_mutual.repository;

import com.bookbook.booklink.chat.chat_mutual.model.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages, UUID> {
    // 특정 채팅방의 메시지를 시간순으로 불러오기
    List<ChatMessages> findByChatIdOrderBySentAtAsc(UUID chatId);
}
