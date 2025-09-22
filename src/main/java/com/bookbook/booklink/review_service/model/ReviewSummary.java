package com.bookbook.booklink.review_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 집계 정보 엔티티")
public class ReviewSummary {

    @Id
    @Schema(description = "리뷰 대상 ID (도서관 또는 사용자)", example = "510e8440-eb9b-11d4-aa16-424651640000")
    private String target_id;

    @Schema(description = "리뷰 대상 유형", example = "LIBRARY")
    private TargetType target_type;

    @Column(nullable = false)
    @Schema(description = "리뷰 총 개수", example = "10")
    private Integer total_count;

    @Column(nullable = false)
    @Schema(description = "리뷰 총 별점 합", example = "42")
    private Long total_rating;

    @Column(nullable = false)
    @Schema(description = "평균 별점", example = "4.2")
    private Double avg_rating;

    @UpdateTimestamp
    @Schema(description = "집계 정보 업데이트 일자", example = "2025-09-22T23:00:00")
    private LocalDateTime updated_at;

    public void addReview(int rating) {
        this.total_count++;
        this.total_rating += rating;
        this.avg_rating = (double) total_rating / total_count;
    }

    public void updateReview(int oldRating, int newRating) {
        this.total_rating = this.total_rating - oldRating + newRating;
        this.avg_rating = (double) total_rating / total_count;
    }

    public void removeReview(int rating) {
        this.total_count--;
        this.total_rating -= rating;
        this.avg_rating = total_count > 0 ? (double) total_rating / total_count : 0.0;
    }
}
