package com.bookbook.booklink.chat_service.single.service;

import com.bookbook.booklink.chat_service.chat_mutual.model.ChatMessages;
import com.bookbook.booklink.chat_service.chat_mutual.model.dto.request.MessageReqDto;
import com.bookbook.booklink.chat_service.chat_mutual.model.dto.response.MessageResDto;
import com.bookbook.booklink.chat_service.chat_mutual.service.ChatMessagesService;
import com.bookbook.booklink.chat_service.single.model.SingleChats;
import com.bookbook.booklink.chat_service.single.model.dto.request.SingleRoomReqDto;
import com.bookbook.booklink.chat_service.single.model.dto.response.SingleRoomResDto;
import com.bookbook.booklink.chat_service.single.repository.SingleChatsRepository;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SingleChatsService {
    private final SingleChatsRepository singleChatsRepository;
    private final ChatMessagesService chatMessagesService;

    /**
     * 두 사용자의 채팅방을 조회하거나 없으면 새로 생성합니다.
     * <p>
     * - user1-user2, user2-user1 조합을 모두 확인합니다. <br>
     * - 기존 채팅방이 없을 경우 새로 생성 후 저장합니다.
     *
     * @param dto 채팅방 생성 요청 DTO (user1Id, user2Id 포함)
     * @return 생성되었거나 조회된 채팅방 응답 DTO
     */
    @Transactional
    public SingleRoomResDto getOrCreateChatRoom(SingleRoomReqDto dto) {

        UUID a = dto.getUser1Id();
        UUID b = dto.getUser2Id();
        UUID u1 = a.compareTo(b) <= 0 ? a : b;
        UUID u2 = a.compareTo(b) <= 0 ? b : a;

        SingleChats chat = singleChatsRepository.findByUser1IdAndUser2Id(u1, u2)
                .orElseGet(() -> singleChatsRepository.save(SingleChats.createNormalized(u1, u2)));

        return SingleRoomResDto.fromEntity(chat);
    }


    /**
     * 새로운 메시지를 저장합니다.
     * <p>
     * - 내부적으로 {@link ChatMessagesService#saveMessages(MessageReqDto)} 호출합니다.
     *
     * @param dto 메시지 요청 DTO
     * @return 저장된 메시지 응답 DTO
     */
    @Transactional
    public MessageResDto saveChatMessages(UUID chatId, UUID senderId, MessageReqDto dto) {
        SingleChats room = singleChatsRepository.findById(chatId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!room.hasMember(senderId)) {
            throw new CustomException(ErrorCode.CHAT_ROOM_FORBIDDEN);
        }

        // senderId 강제 세팅
        MessageReqDto safeDto = new MessageReqDto(chatId, senderId, dto.getText(), dto.getAttachments());

        ChatMessages saved = chatMessagesService.saveMessagesEntity(safeDto);

        room.updateLastMessage(saved.getText(), saved.getSentAt());

        return MessageResDto.fromEntity(saved);
    }

    /**
     * 특정 채팅방의 모든 메시지를 조회합니다.
     * <p>
     * - 내부적으로 {@link ChatMessagesService#findSentMessages(UUID)} 호출합니다.
     *
     * @param chatId 채팅방 UUID
     * @return 메시지 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<MessageResDto> getChatMessages(UUID chatId){
        return chatMessagesService.findSentMessages(chatId);
    }

}
    