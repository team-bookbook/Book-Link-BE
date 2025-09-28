package com.bookbook.booklink.chat.chat_mutual.model;

import com.bookbook.booklink.chat.chat_mutual.code.MessageStatus;
import com.bookbook.booklink.chat.chat_mutual.code.MessageType;
import com.bookbook.booklink.chat.chat_mutual.code.RoomType;
import com.bookbook.booklink.chat.chat_mutual.model.dto.request.MessageReqDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessages {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @Schema(description = "메시지 UUID")
    private UUID id;

    @Lob
    @Schema(description = "메시지 본문", example = "안녕하세요!")
    private String text;

    @Schema(description = "첨부 파일 경로", example = "https://s3.bucket.com/file.png")
    private String fileUrl;

    @CreationTimestamp
    @Schema(description = "전송 시각")
    private LocalDateTime sentAt;

    @Column(nullable = false)
    @Schema(description = "삭제 여부", example = "false")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "메시지 상태", example = "SENT")
    private MessageStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "메시지 타입", example = "TEXT")
    private MessageType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "채팅방 타입", example = "SINGLE")
    private RoomType roomType;

    @Schema(description = "보낸 유저 ID")
    private UUID sendId;

    @Schema(description = "소속 채팅방 ID")
    private UUID chatId;

    public static ChatMessages saveMessage(MessageReqDto dto) {
        return ChatMessages.builder()
                .chatId(dto.getChatId())
                .sendId(dto.getSenderId())
                .text(dto.getText())
                .fileUrl(dto.getFileUrl())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.SENT)
                .type(MessageType.TEXT)
                .isDeleted(false)
                .roomType(RoomType.SINGLE)
                .build();
    }
}
