package com.tinka.reviews.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.products.*;
import com.tinka.common.events.auth.UserDeletedEvent;

public interface ReviewConsumerHandler {

    // Orders → Reviews (eligibility window)
    void onOrderDelivered(OrderDeliveredEvent e);   // enable review eligibility
    void onOrderCancelled(OrderCancelledEvent e);   // disable eligibility
    void onOrderFailed(OrderFailedEvent e);         // disable eligibility

    // Products → Reviews (content lifecycle)
    void onProductDeleted(ProductDeletedEvent e);   // hide/lock reviews for product
    void onProductVerified(ProductVerifiedEvent e); // optional: allow new reviews

    // Auth → Reviews (privacy/compliance)
    void onUserDeleted(UserDeletedEvent e);         // anonymize/purge user reviews
}
