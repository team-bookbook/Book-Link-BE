package com.bookbook.booklink.point_service.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.point_service.controller.docs.PointApiDocs;
import com.bookbook.booklink.point_service.model.dto.request.PointUseDto;
import com.bookbook.booklink.point_service.model.dto.response.PointBalanceDto;
import com.bookbook.booklink.point_service.model.dto.response.PointExchangeDto;
import com.bookbook.booklink.point_service.model.dto.response.PointHistoryListDto;
import com.bookbook.booklink.point_service.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController implements PointApiDocs {
    private final PointService pointService;

    @Override
    public ResponseEntity<BaseResponse<PointBalanceDto>> getPointBalance(
            @RequestParam UUID userId
    ) {
        log.info("[PointController] [userId = {}] Get Point Balance request received.", userId);

        PointBalanceDto balanceDto = pointService.getPointBalance(userId);

        log.info("[PointController] [userId = {}] Get Point Balance success. balance={}", userId, balanceDto.getBalance());

        return ResponseEntity.ok()
                .body(BaseResponse.success(balanceDto));
    }

    @Override
    public ResponseEntity<BaseResponse<PointBalanceDto>> usePoint(
            @RequestParam UUID userId,
            @RequestBody PointUseDto pointUseDto,
            @RequestHeader("Trace-Id") UUID traceId
    ) {
        log.info("[PointController] [userId = {}] Use Point request received. pointUseDto={}", userId, pointUseDto);

        PointBalanceDto balanceDto = pointService.usePoint(pointUseDto, traceId, userId);

        log.info("[PointController] [userId = {}] Use Point success. newBalance={}", userId, balanceDto.getBalance());

        return ResponseEntity.ok()
                .body(BaseResponse.success(balanceDto));
    }

    @Override
    public ResponseEntity<BaseResponse<List<PointHistoryListDto>>> getPointHistory(
            @RequestParam UUID userId
    ) {
        log.info("[PointController] [userId = {}] Get Point History request received.", userId);

        List<PointHistoryListDto> historyListDtoList = pointService.getPointHistoryList(userId);

        log.info("[PointController] [userId = {}] Get Point History success. size={}", userId, historyListDtoList.size());

        return ResponseEntity.ok()
                .body(BaseResponse.success(historyListDtoList));
    }

    @Override
    public ResponseEntity<BaseResponse<Integer>> chargePoint(
            @RequestParam UUID userId,
            @RequestParam String paymentId,
            @RequestHeader("Trace-Id") UUID traceId
    ) {
        log.info("[PointController] [userId = {}] Charge Point request received. paymentId={}", userId, paymentId);

        Integer chargedAmount = pointService.chargePoint(userId, paymentId, traceId);

        log.info("[PointController] [userId = {}] Charge Point success. chargedAmount={}", userId, chargedAmount);

        return ResponseEntity.ok()
                .body(BaseResponse.success(chargedAmount));
    }

    @Override
    public ResponseEntity<BaseResponse<Boolean>> cancelPayment(
            @RequestParam UUID userId,
            @RequestParam String paymentId,
            @RequestParam Integer amount,
            @RequestParam String reason
    ) {
        log.info("[PointController] [userId = {}] Cancel Payment request received. paymentId={}, amount={}, reason={}",
                userId, paymentId, amount, reason);

        pointService.cancelPoint(userId, paymentId, amount, reason);

        log.info("[PointController] [userId = {}] Cancel Payment success. paymentId={}", userId, paymentId);

        return ResponseEntity.ok(BaseResponse.success(Boolean.TRUE));
    }

    @Override
    public ResponseEntity<BaseResponse<PointExchangeDto>> exchangePoint(
            @RequestParam UUID userId,
            @RequestParam Integer num,
            @RequestHeader("Trace-Id") UUID traceId
    ) {
        log.info("[PointController] [userId = {}] Exchange Point request received. num={}, traceId={}",
                userId, num, traceId);

        PointExchangeDto pointExchangeDto = pointService.exchangePoint(userId, traceId, num);

        log.info("[PointController] [userId = {}] Exchange Point success. exchangedPoint={}", userId, pointExchangeDto);

        return ResponseEntity.ok()
                .body(BaseResponse.success(pointExchangeDto));
    }
}