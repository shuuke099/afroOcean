package com.tinka.payments.service;

import com.tinka.payments.dto.PaymentRequest;
import com.tinka.payments.dto.PaymentResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    List<PaymentResponse> getPaymentsByBuyer(String buyerId);

    List<PaymentResponse> getPaymentsBySeller(String sellerId);

    PaymentResponse getPaymentByOrderId(String orderId);
    PaymentResponse refund(Long paymentId, BigDecimal refundAmount, String reason);
}
