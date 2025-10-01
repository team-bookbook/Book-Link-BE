package com.bookbook.booklink.borrow_service.controller;

import com.bookbook.booklink.borrow_service.controller.docs.BorrowApiDocs;
import com.bookbook.booklink.borrow_service.model.dto.request.BorrowRequestDto;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.bookbook.booklink.borrow_service.service.BorrowService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BorrowController implements BorrowApiDocs {
    private final BorrowService borrowService;

    @Override
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
                traceId, userId, borrowId);
        return ResponseEntity.ok(BaseResponse.success(borrowId));
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> requestBorrowConfirmation(
            @RequestParam UUID borrowId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = customUserDetails.getMember().getId();

        log.info("[BorrowController] [traceId = {}, userId = {}] borrow confirm request received, borrowId={}",
                traceId, userId, borrowId);

        // todo 대여 확정을 요청하는 채팅 전송

        log.info("[BorrowController] [traceId = {}, userId = {}] borrow confirm request success, borrowId={}",
                traceId, userId, borrowId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    public ResponseEntity<BaseResponse<Void>> acceptBorrowConfirmation(
            @RequestParam UUID borrowId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = customUserDetails.getMember().getId();

        log.info("[BorrowController] [traceId = {}, userId = {}] accept borrow confirm accept received, borrowId={}",
                traceId, userId, borrowId);

        borrowService.acceptBorrowConfirm(userId, traceId, borrowId);

        log.info("[BorrowController] [traceId = {}, userId = {}] accept borrow confirm request success, borrowId={}",
                traceId, userId, borrowId);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    public ResponseEntity<BaseResponse<Void>> suspendBorrow(
            @RequestParam UUID borrowId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = customUserDetails.getMember().getId();

        log.info("[BorrowController] [traceId = {}, userId = {}] suspend borrow received, borrowId={}",
                traceId, userId, borrowId);

        borrowService.suspendBorrow(userId, traceId, borrowId);

        log.info("[BorrowController] [traceId = {}, userId = {}] accept borrow success, borrowId={}",
                traceId, userId, borrowId);
        return ResponseEntity.ok(BaseResponse.success(null));

    }
}
    