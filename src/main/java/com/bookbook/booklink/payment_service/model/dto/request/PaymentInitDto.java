package com.bookbook.booklink.payment_service.model.dto.request;

import com.bookbook.booklink.payment_service.model.PaymentMethod;
import lombok.Getter;

@Getter
public class PaymentInitDto {

    private String paymentId;

    private Integer amount;

    private PaymentMethod method;
}
