package com.tinka.products.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.common.events.auth.SellerVerifiedEvent;

public interface ProductConsumerHandler {
    // Orders → Products (inventory lifecycle)
    void onOrderPlaced(OrderPlacedEvent e);         // reserve
    void onOrderCancelled(OrderCancelledEvent e);   // release
    void onOrderFailed(OrderFailedEvent e);         // release
    void onOrderShipped(OrderShippedEvent e);       // commit
    void onOrderConfirmed(OrderConfirmedEvent e);   // optional hook if you ever shift reservations

    // Reviews → Products (rating aggregation)
    void onReviewCreated(ReviewCreatedEvent e);

    // Auth → Products (optional seller gating)
    void onSellerVerified(SellerVerifiedEvent e);
}
