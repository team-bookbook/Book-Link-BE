package com.bookbook.booklink.book_service.model;

import com.bookbook.booklink.member.model.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "현재 대출 상태", example = "BORROWED", requiredMode = Schema.RequiredMode.REQUIRED)
    private BorrowStatus status = BorrowStatus.BORROWED;

    @Column(nullable = false)
    @Schema(description = "대여일", example = "2025-09-22T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime borrowedAt;

    @Column(nullable = false)
    @Schema(description = "반납 예정일", example = "2025-09-29T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dueAt;

    @Column(nullable = true)
    @Schema(description = "실제 반납일", example = "2025-09-30T12:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime returnedAt;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "library_book_copy_id", nullable = false)
    private LibraryBookCopy libraryBookCopy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static Borrow createBorrow(LibraryBookCopy copy, Member member, LocalDateTime borrowedAt, LocalDateTime dueAt) {
        return Borrow.builder()
                .libraryBookCopy(copy)
                .member(member)
                .borrowedAt(borrowedAt)
                .dueAt(dueAt)
                .build();
    }

    public void borrow(LocalDateTime borrowedAt, LocalDateTime dueAt) {
        this.status = BorrowStatus.BORROWED;
        this.borrowedAt = borrowedAt;
        this.dueAt = dueAt;
    }

    public void returnBook(LocalDateTime returnedAt, String imageUrl) {
        if (this.status != BorrowStatus.BORROWED && this.status != BorrowStatus.EXTENDED) {
            throw new IllegalStateException("반납할 수 없는 상태입니다.");
        }
        this.status = BorrowStatus.RETURNED;
        this.returnedAt = returnedAt;
        this.imageUrl = imageUrl;
    }

    public void extendBook(LocalDateTime extendedAt) {
        this.status = BorrowStatus.EXTENDED;
        this.dueAt = extendedAt;
    }

    public void overdueBook() {
        this.status = BorrowStatus.OVERDUE;
    }
}
