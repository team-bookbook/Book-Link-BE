package com.bookbook.booklink.payment_service.model.dto.response;

import com.bookbook.booklink.payment_service.model.Payment;
import com.bookbook.booklink.payment_service.model.PaymentMethod;
import com.bookbook.booklink.payment_service.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResDto {

    private Integer amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private LocalDateTime createdAt;

    public static PaymentResDto fromEntity(Payment payment) {
        return PaymentResDto.builder()
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

}
