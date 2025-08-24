package com.tinka.payments.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private String orderId;
    private String buyerId;
    private String sellerId;

    private BigDecimal amount;
    private String currency;

    private String paymentMethod;
    private String transactionReference;
    private String status;

    private LocalDateTime paidAt;

}
