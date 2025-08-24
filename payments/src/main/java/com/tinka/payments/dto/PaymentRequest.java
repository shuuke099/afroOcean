package com.tinka.payments.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private String orderId;
    private String buyerId;
    private String sellerId;

    private BigDecimal amount;
    private String currency;

    private String paymentMethod;

    private String transactionReference;

}
