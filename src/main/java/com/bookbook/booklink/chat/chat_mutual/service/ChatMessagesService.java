package com.bookbook.booklink.chat.chat_mutual.service;

import com.bookbook.booklink.chat.chat_mutual.model.ChatMessages;
import com.bookbook.booklink.chat.chat_mutual.model.dto.request.MessageReqDto;
import com.bookbook.booklink.chat.chat_mutual.model.dto.response.MessageResDto;
import com.bookbook.booklink.chat.chat_mutual.repository.ChatMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {

    private final ChatMessagesRepository chatMessagesRepository;

    /**
     * 특정 채팅방에 속한 모든 메시지를 전송 시간 순으로 조회합니다.
     *
     * @param chatId 채팅방 UUID
     * @return 메시지 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<MessageResDto> findSentMessages(UUID chatId) {
        return chatMessagesRepository.findByChatIdOrderBySentAtAsc(chatId)
                .stream()
                .map(MessageResDto::fromEntity)
                .toList();
    }

    /**
     * 새로운 채팅 메시지를 저장합니다.
     * <p>
     * - 요청 DTO를 엔티티로 변환하여 저장합니다. <br>
     * - 저장된 엔티티를 응답 DTO로 변환하여 반환합니다.
     *
     * @param dto 메시지 요청 DTO
     * @return 저장된 메시지 응답 DTO
     */
    @Transactional
    public MessageResDto saveMessages(MessageReqDto dto) {
        ChatMessages chatMessages = ChatMessages.saveMessage(dto);
        ChatMessages saved = chatMessagesRepository.save(chatMessages);
        return MessageResDto.fromEntity(saved);
    }

}
