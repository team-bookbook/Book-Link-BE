package com.bookbook.booklink.notification_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    // 수신자
    @Column(nullable = false)
    private String userId;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private UUID relatedId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    public void read() {
        this.isRead = Boolean.TRUE;
    }
}
    