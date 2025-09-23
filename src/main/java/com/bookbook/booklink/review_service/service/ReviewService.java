package com.bookbook.booklink.review_service.service;

import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.review_service.model.Review;
import com.bookbook.booklink.review_service.model.ReviewSummary;
import com.bookbook.booklink.review_service.model.dto.request.ReviewCreateDto;
import com.bookbook.booklink.review_service.repository.ReviewSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.review_service.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final IdempotencyService idempotencyService;
    private final ReviewSummaryRepository reviewSummaryRepository;

    /**
     * 도서관/사용자에게 리뷰 생성하는 메서드
     *
     * @param reviewCreateDto 별점, 한줄평이 담긴 dto
     * @param traceId         요청 멱등성 체크용 ID
     * @param userId          요청 사용자 ID
     */
    @Transactional
    public void createReview(ReviewCreateDto reviewCreateDto, String traceId, UUID userId) {

        log.info("[ReviewService] [traceId={}, userId={}] create review initiate. targetId={}",
                traceId, userId, reviewCreateDto.getTarget_id());

        // Redis Lock으로 멱등성 체크
        String key = idempotencyService.generateIdempotencyKey("review:create", traceId);
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // 타입에 따라 review 생성 후 저장
        Review newReview = Review.toEntity(reviewCreateDto);
        Review savedReview = reviewRepository.save(newReview);

        // 리뷰 집계
        ReviewSummary reviewSummary = findReviewSummaryByTargetId(reviewCreateDto.getTarget_id());

        if (reviewSummary == null) {
            // 없으면 새로 생성
            reviewSummary = ReviewSummary.builder()
                    .target_id(reviewCreateDto.getTarget_id())
                    .target_type(reviewCreateDto.getTarget_type())
                    .total_count(1)
                    .total_rating(savedReview.getRating().longValue())
                    .avg_rating(savedReview.getRating().doubleValue())
                    .build();
        } else {
            // 있으면 기존 값에 추가
            reviewSummary.addReview(savedReview.getRating());
        }

        reviewSummaryRepository.save(reviewSummary);

        log.info("[ReviewService] [traceId={}, userId={}] create review success. target={}",
                traceId, userId, reviewCreateDto.getTarget_id());
    }

    /**
     * ID로 리뷰 집계 조회
     *
     * @param targetId 조회할 ID
     * @return 리뷰 집계 혹은 null
     */
    public ReviewSummary findReviewSummaryByTargetId(String targetId) {

        return reviewSummaryRepository.findById(targetId).orElse(null);
    }

    /**
     * ID로 리뷰 조회
     *
     * @param reviewId 조회할 ID
     * @return 리뷰
     */
    public Review findReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

}
    