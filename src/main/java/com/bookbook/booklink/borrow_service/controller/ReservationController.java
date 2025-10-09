package com.bookbook.booklink.borrow_service.controller;

import com.bookbook.booklink.borrow_service.controller.docs.ReservationApiDocs;
import com.bookbook.booklink.borrow_service.service.ReservationService;
import com.bookbook.booklink.common.dto.BaseResponse;
import com.bookbook.booklink.common.jwt.CustomUserDetail.CustomUserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReservationController implements ReservationApiDocs {
    private final ReservationService reservationService;

    @Override
    public ResponseEntity<BaseResponse<UUID>> reserveBook(
            @NotNull @RequestParam UUID libraryBookId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestHeader("Trace-Id") String traceId
    ) {
        UUID userId = customUserDetails.getMember().getId();

        log.info("[ReservationController] [traceId = {}, userId = {}] reserve book request received, libraryBookId={}",
                traceId, userId, libraryBookId);

        UUID reservationId = reservationService.reserveBook(userId, traceId, libraryBookId);

        log.info("[ReservationController] [traceId = {}, userId = {}] reserve book request success, reservationId={}",
                traceId, userId, reservationId);
        return ResponseEntity.ok(BaseResponse.success(reservationId));

    }
}
