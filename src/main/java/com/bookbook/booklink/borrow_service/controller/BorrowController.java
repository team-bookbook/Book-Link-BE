package com.bookbook.booklink.borrow_service.controller;

import com.bookbook.booklink.book_service.model.dto.response.BookResponseDto;
import com.bookbook.booklink.borrow_service.controller.docs.BorrowRequestDto;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.borrow_service.service.BorrowService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    public ResponseEntity<BaseResponse<UUID>> borrowBook(
            @Valid @RequestBody BorrowRequestDto borrowRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = customUserDetails.getMember().getId();

        log.info("[BorrowController] [traceId = {}, userId = {}] borrow book request received, borrowRequestDto={}",
                traceId, userId, borrowRequestDto);

        UUID borrowId = borrowService.borrowBook(userId, traceId, borrowRequestDto);

        log.info("[BorrowController] [traceId = {}, userId = {}] borrow book request success, borrowId={}",
                traceId, userId, null);
        return ResponseEntity.ok(BaseResponse.success(null));
    }
}
    