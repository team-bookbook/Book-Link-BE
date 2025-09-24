package com.bookbook.booklink.book_service.model;

import com.bookbook.booklink.book_service.model.dto.request.LibraryBookRegisterDto;
import com.bookbook.booklink.library_service.model.Library;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LibraryBook {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    @Schema(description = "도서관이 소장한 도서 고유 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Getter
    private UUID id;

    @Min(0)
    @Column(nullable = false)
    @Schema(description = "보유 권수", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer copies;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "보증금 (단위 : 포인트)", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer deposit = 0;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "빌린 횟수", example = "14", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalBorrowCount = 0;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "대여한 사람 수", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer borrowedCount = 0;

    @Min(0)
    @Column(nullable = false)
    @Schema(description = "대여 가능한 도서 수", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer availableBooks;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Schema(description = "도서 등록일", example = "2025-09-22T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

/*    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    @Schema(description = "이 도서를 소장한 도서관")
    private Library library;*/

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @Schema(description = "도서 정보")
    private Book book;

    @OneToMany(mappedBy = "libraryBook", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Schema(description = "도서관이 보유한 각 권 개별 도서")
    private List<LibraryBookCopy> copiesList = new ArrayList<>();

    public static LibraryBook toEntity(LibraryBookRegisterDto libraryBookRegisterDto, Book book
//            , Library library
    ) {
        return LibraryBook.builder()
                .copies(libraryBookRegisterDto.getCopies())
                .availableBooks(libraryBookRegisterDto.getCopies())
                .deposit(libraryBookRegisterDto.getDeposit())
                .book(book)
//                .library(library)
                .build();
    }

    public void addCopy(LibraryBookCopy copy) {
        copiesList.add(copy);
    }

    public void updateAvailableBooks() {
        this.availableBooks = Math.max(0, copies - borrowedCount);
    }
}
