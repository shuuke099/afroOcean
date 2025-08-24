package com.tinka.orders.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumerHandlerImpl implements OrderConsumerHandler {

    // Inject your OrderService/Repository/etc. here when ready
    // private final OrderService orderService;

    // --- Auth ---
    @Override
    public void onUserCreated(String userId, String fullName, String email, String role, Instant createdAt) {
        log.info("Auth user.created userId={} fullName={} email={} role={} createdAt={}",
                userId, fullName, email, role, createdAt);
    }

    @Override
    public void onUserUpdated(String userId, String fullName, String email, String role, Instant updatedAt) {
        log.info("Auth user.updated userId={} fullName={} email={} role={} updatedAt={}",
                userId, fullName, email, role, updatedAt);
    }

    @Override
    public void onUserDeleted(String userId, Instant deletedAt) {
        log.info("Auth user.deleted userId={} deletedAt={}", userId, deletedAt);
    }

    @Override
    public void onSellerVerified(String sellerId, Instant verifiedAt) {
        log.info("Auth seller.verified sellerId={} verifiedAt={}", sellerId, verifiedAt);
    }

    // --- Products ---
    @Override
    public void onProductCreated(String productId) { log.info("Product created productId={}", productId); }

    @Override
    public void onProductUpdated(String productId) { log.info("Product updated productId={}", productId); }

    @Override
    public void onProductDeleted(String productId) { log.info("Product deleted productId={}", productId); }

    @Override
    public void onProductVerified(String productId) { log.info("Product verified productId={}", productId); }

    @Override
    public void onProductOutOfStock(String productId) { log.warn("Product OOS productId={}", productId); }

    // --- Payments ---
    @Override
    public void onPaymentInitiated(String orderId, String paymentId) {
        log.info("Payment initiated orderId={} paymentId={}", orderId, paymentId);
    }

    @Override
    public void onPaymentProcessed(String orderId, String paymentId, BigDecimal amount, String currency, String txRef) {
        log.info("Payment processed orderId={} paymentId={} amount={} {} txRef={}",
                orderId, paymentId, amount, currency, txRef);
    }

    @Override
    public void onPaymentFailed(String orderId, String paymentId, String reason) {
        log.warn("Payment failed orderId={} paymentId={} reason={}", orderId, paymentId, reason);
    }

    @Override
    public void onRefundIssued(String orderId, String paymentId, BigDecimal amount, String currency) {
        log.info("Refund issued orderId={} paymentId={} amount={} {}", orderId, paymentId, amount, currency);
    }

    // --- Reviews (optional) ---
    @Override
    public void onReviewCreated(String productId, String reviewId, String reviewerId, Number rating, String comment) {
        log.info("Review created productId={} reviewId={} reviewerId={} rating={} comment={}",
                productId, reviewId, reviewerId, rating, comment);
    }
}
