package com.bookbook.booklink.review_service.controller.docs;


import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.review_service.model.dto.request.ReviewCreateDto;
import com.bookbook.booklink.review_service.model.dto.request.ReviewUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/review")
@Tag(name = "Review API", description = "리뷰 등록/조회/수정 관련 API")
public interface ReviewApiDocs {


    @Operation(
            summary = "리뷰 생성",
            description = "사용자/도서관에 대한 리뷰를 생성합니다. " +
                    "하나의 거래(도서 대여)당 하나의 리뷰가 가능합니다."
    )
    @ApiErrorResponses({ErrorCode.VALIDATION_FAILED, ErrorCode.DATABASE_ERROR,
            ErrorCode.METHOD_UNAUTHORIZED, ErrorCode.DATA_INTEGRITY_VIOLATION})

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> createReview(
            @Valid @RequestBody ReviewCreateDto reviewCreateDto,
            @RequestHeader("Trace-Id") String traceId
    );

    @Operation(
            summary = "리뷰 수정",
            description = "사용자/도서관에 대한 리뷰를 수정합니다. 평점, 코멘트 수정가능"
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<BaseResponse<Boolean>> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewUpdateDto reviewUpdateDto,
            @RequestHeader("Trace-Id") String traceId
    );
}
