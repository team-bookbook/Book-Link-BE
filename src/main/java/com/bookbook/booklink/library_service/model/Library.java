package com.bookbook.booklink.library_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Library {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @Schema(description = "도서관 고유 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Column(nullable = false, length = 20)
    @Size(min = 2, max = 20)
    @Schema(description = "도서관 이름", example = "강남 책방", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Column(nullable = false, length = 200)
    @Size(min = 10, max = 200)
    @Schema(description = "도서관 소개", example = "강남에 위치한 아늑한 독립 서점입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "도서관 리뷰 별점 평균", example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double stars = 0.0;

    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "도서관이 받은 리뷰의 수", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer review_count = 0;

    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "도서관이 받은 좋아요의 수", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer like_count = 0;

    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "도서관이 보유한 책의 수", example = "120", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer book_count = 0;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "도서관 생성 일자", example = "2025-09-19T23:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime created_at;

    @Column
    @Schema(description = "도서관 썸네일 URL", example = "https://example.com/thumbnail.jpg", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String thumbnail_url;

    @Column(nullable = false)
    @Schema(description = "도서관 위도", example = "37.4979", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double latitude;

    @Column(nullable = false)
    @Schema(description = "도서관 경도", example = "127.0276", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double longitude;

    @Column(nullable = false)
    @Schema(description = "영업 시작 시간", example = "09:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime start_time;

    @Column(nullable = false)
    @Schema(description = "영업 종료 시간", example = "21:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime end_time;

    // TODO: User와 1:1 연결 필요
}