package com.bookbook.booklink.borrow_service.controller.docs;

import com.bookbook.booklink.borrow_service.model.dto.request.BorrowRequestDto;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Borrow API", description = "도서 대여 관련 API")
@RequestMapping("/api/borrow")
public interface BorrowApiDocs {

    @Operation(
            summary = "도서 대여",
            description = "도서를 대여합니다."
    )
    @ApiErrorResponses({ErrorCode.DATABASE_ERROR, ErrorCode.BOOK_NOT_FOUND,
            ErrorCode.USER_NOT_FOUND, ErrorCode.N0T_AVAILABLE_COPY})
    @PostMapping
    public ResponseEntity<BaseResponse<UUID>> borrowBook(
            @Valid @RequestBody BorrowRequestDto borrowRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    );

    @Operation(
            summary = "대여 확정 요청",
            description = "대여 확정 요청 채팅을 전송합니다."
    )
    @ApiErrorResponses({ErrorCode.DATABASE_ERROR /*todo 에러 코드 추가*/})
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> requestBorrowConfirmation(
            @RequestParam UUID borrowId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    );
}
