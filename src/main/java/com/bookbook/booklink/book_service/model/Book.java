package com.bookbook.booklink.book_service.model;

import com.bookbook.booklink.book_service.model.dto.request.BookRegDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @Schema(description = "도서 고유 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    @Getter
    private UUID id;

    @Column(nullable = false, length = 64)
    @Size(min = 1, max = 64)
    @Schema(description = "도서 이름", example = "마흔에 읽는 쇼펜하우어", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 64)
    private String name;

    @Column(nullable = false, length = 16)
    @Size(min = 1, max = 16)
    @Schema(description = "저자명", example = "강용수", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 16)
    private String author;

    @Column(nullable = false, length = 16)
    @Size(min = 1, max = 16)
    @Schema(description = "출판사", example = "유노북스", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 16)
    private String publisher;

    @Column(nullable = false)
    @NotBlank()
    @Schema(description = "카테고리", example = "고전문학", requiredMode = Schema.RequiredMode.REQUIRED)
    private BookCategory category;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "대출된 총 횟수", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer borrow_count = 0;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "보증금 (단위 : 포인트)", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer rent_price = 0;

    @Column(nullable = false, unique = true, length = 13)
    @Schema(description = "ISBN 코드", example = "9791192300818", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 13, max = 13)
    private String ISBN;

    @Min(0)
    @Column(nullable = false)
    @Schema(description = "도서 정가", example = "17000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer original_price;

    @Column(nullable = false)
    @Schema(description = "대여 가능 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean can_borrow;

    @Min(0)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "좋아요 수", example = "14", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer like_count = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Schema(description = "도서 등록일", example = "2025-09-22T12:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime created_at;

    // todo : 등록된 도서관 1:N 매칭

    // todo : 카테고리와 1:N 매칭

    public static Book toEntity(BookRegDto bookRegDto) {
        return Book.builder()
                .name(bookRegDto.getName())
                .author(bookRegDto.getAuthor())
                .publisher(bookRegDto.getPublisher())
                .category(BookCategory.getByCode(bookRegDto.getCategory()))
                .rent_price(bookRegDto.getRentPrice())
                .ISBN(bookRegDto.getISBN())
                .original_price(bookRegDto.getOriginalPrice())
                .can_borrow(bookRegDto.isCanBorrow())
                .build();
    }
}
    