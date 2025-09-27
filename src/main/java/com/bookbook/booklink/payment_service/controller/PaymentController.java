package com.bookbook.booklink.payment_service.controller;

import com.bookbook.booklink.common.exception.BaseResponse;
import com.bookbook.booklink.payment_service.controller.docs.PaymentApiDocs;
import com.bookbook.booklink.payment_service.model.dto.request.PaymentInitDto;
import com.bookbook.booklink.payment_service.model.dto.response.PaymentResDto;
import com.bookbook.booklink.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentApiDocs {
    private final PaymentService paymentService;

    @Override
    public ResponseEntity<BaseResponse<Boolean>> initPayment(PaymentInitDto paymentInitDto) {
        log.info("[PaymentController] Init payment request received. paymentId={}, amount={}, method={}",
                paymentInitDto.getPaymentId(), paymentInitDto.getAmount(), paymentInitDto.getMethod());

        paymentService.initPayment(paymentInitDto);

        log.info("[PaymentController] Init payment success. paymentId={}", paymentInitDto.getPaymentId());

        return ResponseEntity.ok()
                .body(BaseResponse.success(Boolean.TRUE));
    }

    @Override
    public ResponseEntity<BaseResponse<PaymentResDto>> getPayment(String paymentId) {
        log.info("[PaymentController] Get payment request received. paymentId={}", paymentId);

        PaymentResDto paymentRes = paymentService.getPayment(paymentId);

        log.info("[PaymentController] Get payment success. paymentId={}", paymentId);

        return ResponseEntity.ok(BaseResponse.success(paymentRes));
    }

    @Override
    public ResponseEntity<BaseResponse<List<PaymentResDto>>> getPaymentsByUser(UUID userId) {
        log.info("[PaymentController] Get payments by user request received. userId={}", userId);

        List<PaymentResDto> payments = paymentService.getPaymentsByUser(userId);

        log.info("[PaymentController] Get payments by user success. userId={}, count={}", userId, payments.size());

        return ResponseEntity.ok(BaseResponse.success(payments));
    }
}
