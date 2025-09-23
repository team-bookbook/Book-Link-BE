package com.bookbook.booklink.review_service.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.review_service.controller.docs.ReviewApiDocs;
import com.bookbook.booklink.review_service.model.dto.request.ReviewCreateDto;
import com.bookbook.booklink.review_service.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApiDocs {
    private final ReviewService reviewService;

    @Override
    public ResponseEntity<BaseResponse<Boolean>> createReview(
            @Valid @RequestBody ReviewCreateDto reviewCreateDto,
            @RequestHeader("Trace-Id") String traceId
    ) {
        //todo: 실제 user Id로 변경해야함
        UUID userId = UUID.randomUUID();

        log.info("[ReviewController] [traceId = {}, userId = {}] create review request received. targetId={}",
                traceId, userId, reviewCreateDto.getTarget_id());

        reviewService.createReview(reviewCreateDto, traceId, userId);

        log.info("[ReviewController] [traceId = {}, userId = {}] create review response success. targetId={}",
                traceId, userId, reviewCreateDto.getTarget_id());

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }
}
    