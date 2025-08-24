package com.tinka.orders.service;

import java.math.BigDecimal;
import java.time.Instant;

public interface OrderConsumerHandler {
    // --- Auth ---
    void onUserCreated(String userId, String fullName, String email, String role, Instant createdAt);
    void onUserUpdated(String userId, String fullName, String email, String role, Instant updatedAt); // any nullable is fine
    void onUserDeleted(String userId, Instant deletedAt);
    void onSellerVerified(String sellerId, Instant verifiedAt);

    // --- Products ---
    void onProductCreated(String productId);
    void onProductUpdated(String productId);
    void onProductDeleted(String productId);
    void onProductVerified(String productId);
    void onProductOutOfStock(String productId);

    // --- Payments ---
    void onPaymentInitiated(String orderId, String paymentId);
    void onPaymentProcessed(String orderId, String paymentId, BigDecimal amount, String currency, String txRef);
    void onPaymentFailed(String orderId, String paymentId, String reason);
    void onRefundIssued(String orderId, String paymentId, BigDecimal amount, String currency);

    // --- Reviews (optional) ---
    void onReviewCreated(String productId, String reviewId, String reviewerId, Number rating, String comment);
}
