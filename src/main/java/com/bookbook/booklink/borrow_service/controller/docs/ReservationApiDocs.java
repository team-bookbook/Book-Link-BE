package com.bookbook.booklink.borrow_service.controller.docs;

import com.bookbook.booklink.borrow_service.model.dto.request.BorrowRequestDto;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.exception.ApiErrorResponses;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Reservation API", description = "도서 예약 관련 API")
@RequestMapping("/api/reservations")
public interface ReservationApiDocs {
    @Operation(
            summary = "도서 예약",
            description = "도서 하나를 예약합니다."
    )
    @ApiErrorResponses({ErrorCode.DATABASE_ERROR, /* todo 에러 코드 추가 */})
    @PostMapping()
    public ResponseEntity<BaseResponse<UUID>> reserveBook(
            @NotNull @RequestParam UUID libraryBookId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    );
}
