package com.bookbook.booklink.notification_service.model.dto.response;

import com.bookbook.booklink.notification_service.model.Notification;
import com.bookbook.booklink.notification_service.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResDto {

    private UUID id;

    // 수신자
    private String userId;

    private String message;

    private NotificationType type;

    private UUID relatedId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    public static NotificationResDto fromEntity(Notification notification) {
        return NotificationResDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .type(notification.getType())
                .relatedId(notification.getRelatedId())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
