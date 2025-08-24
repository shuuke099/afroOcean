package com.tinka.payments.service;

import com.tinka.common.events.orders.*;
import com.tinka.common.events.auth.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentConsumerHandlerImpl implements PaymentConsumerHandler {

    // private final PaymentService paymentService; // inject when ready

    // --- Orders → Payments ---
    @Override
    public void onOrderPlaced(OrderPlacedEvent e) {
        log.info("Payments: order.placed orderId={}", e.getOrderId());
        // paymentService.authorize(e);
    }

    @Override
    public void onOrderCancelled(OrderCancelledEvent e) {
        log.info("Payments: order.cancelled orderId={}", e.getOrderId());
        // paymentService.voidAuthorization(e);
    }

    @Override
    public void onOrderShipped(OrderShippedEvent e) {
        log.info("Payments: order.shipped (capture funds) orderId={}", e.getOrderId());
        // paymentService.capture(e);
    }

    @Override
    public void onOrderFailed(OrderFailedEvent e) {
        log.warn("Payments: order.failed orderId={}", e.getOrderId());
        // paymentService.markFailed(e);
    }

    @Override
    public void onOrderConfirmed(OrderConfirmedEvent e) {
        log.info("Payments: order.confirmed orderId={}", e.getOrderId());
        // If you ever move authorization to Confirmed, do it here.
    }

    // --- Auth → Payments ---
    @Override
    public void onSellerVerified(SellerVerifiedEvent e) {
        log.info("Payments: seller.verified sellerId={}",
                e.getSellerId() != null ? e.getSellerId() : e.getUserId());
        // paymentService.markSellerPayable(e);
    }
    @Override
    public void onUserDeleted(UserDeletedEvent e) {
        log.info("Payments: user.deleted userId={} deletedAt={}", e.getUserId(), e.getDeletedAt());

    }
}
