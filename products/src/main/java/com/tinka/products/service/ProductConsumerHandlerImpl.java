package com.tinka.products.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.common.events.auth.SellerVerifiedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductConsumerHandlerImpl implements ProductConsumerHandler {

    // private final InventoryService inventory; // inject your domain services when ready
    // private final RatingService ratingService;

    // --- Orders → Products ---
    @Override
    public void onOrderPlaced(OrderPlacedEvent e) {
        log.info("Products: order.placed reserve items orderId={}", e.getOrderId());
        // inventory.reserve(e.getOrderId(), e.getItems());
    }

    @Override
    public void onOrderCancelled(OrderCancelledEvent e) {
        log.info("Products: order.cancelled release reservation orderId={}", e.getOrderId());
        // inventory.release(e.getOrderId());
    }

    @Override
    public void onOrderFailed(OrderFailedEvent e) {
        log.warn("Products: order.failed release reservation orderId={}", e.getOrderId());
        // inventory.release(e.getOrderId());
    }

    @Override
    public void onOrderShipped(OrderShippedEvent e) {
        log.info("Products: order.shipped commit reservation orderId={}", e.getOrderId());
        // inventory.commit(e.getOrderId());
    }

    @Override
    public void onOrderConfirmed(OrderConfirmedEvent e) {
        log.info("Products: order.confirmed (optional hook) orderId={}", e.getOrderId());
        // If you move reservation here in the future, do it here.
    }

    // --- Reviews → Products ---
    @Override
    public void onReviewCreated(ReviewCreatedEvent e) {
        log.info("Products: review.created productId={} rating={}", e.getProductId(), e.getRating());
        // ratingService.apply(e.getProductId(), e.getReviewerId(), e.getRating(), e.getComment());
    }

    // --- Auth → Products (optional) ---
    @Override
    public void onSellerVerified(SellerVerifiedEvent e) {
        var sid = e.getSellerId() != null ? e.getSellerId() : e.getUserId();
        log.info("Products: seller.verified sellerId={}", sid);
        // productService.enableSellerPublishing(sid);
    }
}
