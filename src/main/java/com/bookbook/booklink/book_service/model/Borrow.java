package com.bookbook.booklink.book_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    @Schema(description = "대여 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Getter
    private UUID id;

    private BorrowStatus borrowStatus;

    @Schema(description = "대여일", example = "2025-09-22T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime borrowedAt;

    @Schema(description = "반납 예정일", example = "2025-09-29T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dueAt;

    @Schema(description = "실제 반납일", example = "2025-09-30T12:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime returnedAt;

    // todo : 도서 한개가 여러번 빌릴 수 있다. ManyToOne

    // todo : 회원 한 명이 여러 번 빌릴 수 있다. ManyToOne

    private String imageUrl;
}
