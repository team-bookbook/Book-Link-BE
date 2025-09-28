package com.bookbook.booklink.chat.single.model;

import com.bookbook.booklink.chat.chat_mut.code.ChatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneToOneChats {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @Schema(description = "채팅방 UUID",example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "채팅방 상태", example = "ACTIVE")
    private ChatStatus status;

    @Schema(description = "마지막 메시지 내용", example = "감사합니다.")
    private String lastMessage;

    @Schema(description = "마지막 메시지 전송 시각",example = "2025-09-28T15:30:00")
    private LocalDateTime lastSentAt;

    @CreationTimestamp
    @Schema(description = "생성 시각",example = "2025-09-28T15:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "참여자 1 ID",example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID user1Id;

    @Schema(description = "참여자 2 ID", example = "7fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID user2Id;
}
    