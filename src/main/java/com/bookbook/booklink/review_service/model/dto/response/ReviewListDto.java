package com.bookbook.booklink.review_service.model.dto.response;

import com.bookbook.booklink.review_service.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "리뷰 응답 DTO")
public class ReviewListDto {
    @Schema(description = "리뷰 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID reviewId;

    @Schema(description = "작성자 이름", example = "test@test.com")
    private String reviewerName;

    @Schema(description = "별점", example = "4")
    private Short rating;

    @Schema(description = "코멘트", example = "전체적으로 책 상태가 깔끔해요!")
    private String comment;

    public static ReviewListDto fromEntity(Review review) {
        return ReviewListDto.builder()
                .reviewId(review.getId())
                .reviewerName(review.getReviewer().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
