package com.tinka.reviews.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.products.*;
import com.tinka.common.events.auth.UserDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewConsumerHandlerImpl implements ReviewConsumerHandler {

    // private final ReviewService reviewService; // inject when ready

    // --- Orders ---
    @Override
    public void onOrderDelivered(OrderDeliveredEvent e) {
        log.info("Reviews: order.delivered enable eligibility orderId={}", e.getOrderId());
        // reviewService.enableEligibility(e.getOrderId(), e.getBuyerId(), e.getItems());
    }

    @Override
    public void onOrderCancelled(OrderCancelledEvent e) {
        log.info("Reviews: order.cancelled disable eligibility orderId={}", e.getOrderId());
        // reviewService.disableEligibility(e.getOrderId());
    }

    @Override
    public void onOrderFailed(OrderFailedEvent e) {
        log.warn("Reviews: order.failed disable eligibility orderId={}", e.getOrderId());
        // reviewService.disableEligibility(e.getOrderId());
    }

    // --- Products ---
    @Override
    public void onProductDeleted(ProductDeletedEvent e) {
        log.info("Reviews: product.deleted lock/hide reviews productId={}", e.getProductId());
        // reviewService.lockProductReviews(e.getProductId());
    }

    @Override
    public void onProductVerified(ProductVerifiedEvent e) {
        log.info("Reviews: product.verified allow reviews productId={}", e.getProductId());
        // reviewService.allowProductReviews(e.getProductId());
    }

    // --- Auth ---
    @Override
    public void onUserDeleted(UserDeletedEvent e) {
        log.info("Reviews: user.deleted anonymize/purge userId={}", e.getUserId());
        // reviewService.handleUserDeletion(e.getUserId());
    }
}
