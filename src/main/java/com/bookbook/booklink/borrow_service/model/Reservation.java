package com.bookbook.booklink.borrow_service.model;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.book_service.model.LibraryBook;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    @Schema(description = "예약 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Getter
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "library_book_id", nullable = false)
    private LibraryBook libraryBook;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Schema(description = "예약 일시", example = "2025-09-30T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime reservedAt;

    @Schema(description = "만료 일시", example = "2025-10-01T12:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime availableUntil;

    @Schema(description = "예약 상태", example = "AVAILABLE", requiredMode = Schema.RequiredMode.REQUIRED)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.WAITING;

    public static Reservation toEntity(Member member, LibraryBook libraryBook) {
        return Reservation.builder()
                .member(member)
                .libraryBook(libraryBook)
                .build();
    }

    public void updateStatus(ReservationStatus newStatus) {
        if (!status.canTransitionTo(newStatus)) {
            throw new CustomException(ErrorCode.INVALID_RESERVATION_STATUS_TRANSITION);
        }
        this.status = newStatus;
    }

    public void setAvailable() {
        availableUntil = LocalDateTime.now().plusDays(1);
        updateStatus(ReservationStatus.AVAILABLE);
    }
}
