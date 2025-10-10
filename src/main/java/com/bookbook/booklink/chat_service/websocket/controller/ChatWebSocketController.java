package com.bookbook.booklink.chat_service.websocket.controller;

import com.bookbook.booklink.chat_service.chat_mutual.model.dto.request.MessageReqDto;
import com.bookbook.booklink.chat_service.chat_mutual.model.dto.response.MessageResDto;
import com.bookbook.booklink.chat_service.single.service.SingleChatsService;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SingleChatsService singleChatsService;
    private final SimpMessagingTemplate messagingTemplate;

    // 클라이언트가 /pub/chat.send 로 메시지 발행
    @MessageMapping("/chat/send")
    public void sendMessage(
            MessageReqDto dto,
            // @AuthenticationPrincipal CustomUserDetails userDetails
            Principal principal
    ) {

        System.out.println("🎯 Controller 진입, principal = " + principal);
        if (principal == null) {
            System.out.println("❌ principal is NULL");
            return;
        }


        CustomUserDetails userDetails = (CustomUserDetails) principal;
        UUID memberId = userDetails.getMember().getId();
        String email = userDetails.getUsername();

        MessageResDto saved = singleChatsService.saveChatMessages(memberId, dto);

        // senderEmail을 직접 세팅해서 클라이언트로 전달
        saved = MessageResDto.builder()
                .chatId(saved.getChatId())
                .senderId(saved.getSenderId())
                .text(saved.getText())
                .sentAt(saved.getSentAt())
                .status(saved.getStatus())
                .type(saved.getType())
                .attachments(saved.getAttachments())
                .build();

        // 구독자에게 메시지 전달
        messagingTemplate.convertAndSend("/sub/chat/" + dto.getChatId(), saved);
        System.out.println("✅ Controller 인증 유저 ID = " + memberId);
    }
}