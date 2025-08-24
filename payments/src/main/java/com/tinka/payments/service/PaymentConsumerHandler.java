package com.tinka.payments.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.auth.*;

public interface PaymentConsumerHandler {
    // Orders → Payments
    void onOrderPlaced(OrderPlacedEvent e);         // authorize intent/hold
    void onOrderCancelled(OrderCancelledEvent e);   // void/release
    void onOrderShipped(OrderShippedEvent e);       // capture (our policy)
    void onOrderFailed(OrderFailedEvent e);         // mark failed

    // Optional (keep the hook even if you don't use it yet)
    void onOrderConfirmed(OrderConfirmedEvent e);   // if you ever move auth here

    // Auth → Payments (compliance/payout gating)
    void onSellerVerified(SellerVerifiedEvent e);   // mark seller payable
    void onUserDeleted(UserDeletedEvent e);
}
