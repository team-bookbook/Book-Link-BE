package com.bookbook.booklink.review_service.service;

import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import com.bookbook.booklink.review_service.model.Review;
import com.bookbook.booklink.review_service.model.ReviewSummary;
import com.bookbook.booklink.review_service.model.dto.request.ReviewCreateDto;
import com.bookbook.booklink.review_service.model.dto.request.ReviewUpdateDto;
import com.bookbook.booklink.review_service.model.dto.response.ReviewListDto;
import com.bookbook.booklink.review_service.repository.ReviewRepository;
import com.bookbook.booklink.review_service.repository.ReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                traceId, userId, reviewCreateDto.getTargetId());

        // Redis Lock으로 멱등성 체크
        String key = idempotencyService.generateIdempotencyKey("review:create", traceId);
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // 타입에 따라 review 생성 후 저장
        Review newReview = Review.toEntity(reviewCreateDto);
        Review savedReview = reviewRepository.save(newReview);

        // 리뷰 집계
        ReviewSummary reviewSummary = findReviewSummaryByTargetId(reviewCreateDto.getTargetId());

        if (reviewSummary == null) {
            // 없으면 새로 생성
            reviewSummary = ReviewSummary.toEntity(
                    reviewCreateDto,
                    savedReview.getRating().longValue(),
                    savedReview.getRating().doubleValue()
            );
        } else {
            // 있으면 기존 값에 추가
            reviewSummary.addReview(savedReview.getRating());
        }

        reviewSummaryRepository.save(reviewSummary);

        log.info("[ReviewService] [traceId={}, userId={}] create review success. target={}",
                traceId, userId, reviewCreateDto.getTargetId());
    }

    /**
     * 리뷰 수정하는 메서드
     *
     * @param reviewUpdateDto 리뷰의 수정 정보 Dto
     * @param reviewId        수정할 리뷰의 ID
     * @param traceId         요청 멱등성 체크용 ID
     * @param userId          요청 사용자 ID
     */
    @Transactional
    public void updateReview(ReviewUpdateDto reviewUpdateDto, UUID reviewId, String traceId, UUID userId) {

        log.info("[ReviewService] [traceId={}, userId={}] update review initiate. reviewId={}",
                traceId, userId, reviewId);

        // Redis Lock으로 멱등성 체크
        String key = idempotencyService.generateIdempotencyKey("review:update", traceId);
        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        // 기존 리뷰 조회
        Review existingReview = findReviewById(reviewId);

        Short oldRating = existingReview.getRating();
        Short newRating = reviewUpdateDto.getRating();

        ReviewSummary reviewSummary = findReviewSummaryByTargetId(existingReview.getTargetId());
        reviewSummary.updateReview(oldRating, newRating);

        // 리뷰 별점, 코멘트 수정
        existingReview.updateReview(reviewUpdateDto);

        reviewSummaryRepository.save(reviewSummary);
        reviewRepository.save(existingReview);

        log.info("[ReviewService] [traceId={}, userId={}] update review success. reviewId={}",
                traceId, userId, reviewId);
    }

    /**
     * 리뷰 삭제하는 메서드
     *
     * @param reviewId 삭제할 리뷰의 ID
     * @param userId   요청한 유저의 ID
     */
    @Transactional
    public void deleteReview(UUID traceId, UUID reviewId, UUID userId) {
        log.info("[ReviewService] [traceId={}, userId={}] delete review initiate. reviewId={}",
                traceId, userId, reviewId);

        // 리뷰 삭제
        Review existingReview = findReviewById(reviewId);
        String targetId = existingReview.getTargetId();
        reviewRepository.delete(existingReview);

        // 리뷰 집계에 반영
        ReviewSummary reviewSummary = findReviewSummaryByTargetId(targetId);
        reviewSummary.removeReview(existingReview.getRating());
        reviewSummaryRepository.save(reviewSummary);

        log.info("[ReviewService] [traceId={}, userId={}] delete review success. reviewId={}",
                traceId, userId, reviewId);
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

    /**
     * 도서관에 달린 모든 리뷰의 목록을 조회하는 메서드
     *
     * @param libraryId 조회할 도서관의 ID
     * @return 리뷰 목록 dto
     */
    @Transactional(readOnly = true)
    public List<ReviewListDto> getLibraryReview(UUID libraryId) {

        List<Review> libraryReviewList = reviewRepository.findAllByTargetId(libraryId.toString());

        return libraryReviewList.stream().map(ReviewListDto::fromEntity).toList();
    }

    /**
     * 유저/도서관의 평균 별점을 조회하는 메서드
     *
     * @param targetId 조회할 유저/도서관의 ID
     * @return 평균 별점 or null
     */
    @Transactional(readOnly = true)
    public Double getAvgRating(String targetId) {

        ReviewSummary reviewSummary = findReviewSummaryByTargetId(targetId);

        if (reviewSummary == null) {
            return null;
        } else {
            return reviewSummary.getAvgRating();
        }

    }

}
    