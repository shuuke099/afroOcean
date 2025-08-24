package com.tinka.orders.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.payments.PaymentFailedEvent;
import com.tinka.common.events.payments.PaymentInitiatedEvent;
import com.tinka.common.events.payments.PaymentProcessedEvent;
import com.tinka.common.events.payments.RefundIssuedEvent;
import com.tinka.orders.service.OrderConsumerHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

/**
 * Consumes payment-related domain events and delegates to OrderConsumerHandler.
 * Uses the "orders-payments" group id.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventsConsumer {

    // Injected collaborators (constructor-injected via Lombok)
    private final EventDeserializer deserializer;
    private final OrderConsumerHandler handler;

    // Built after construction (depend on injected collaborators)
    private BaseKafkaConsumer<PaymentInitiatedEvent> initiated;
    private BaseKafkaConsumer<PaymentProcessedEvent> processed;
    private BaseKafkaConsumer<PaymentFailedEvent> failed;
    private BaseKafkaConsumer<RefundIssuedEvent> refunded;

    @PostConstruct
    void init() {
        // Build strongly-typed consumers with single-responsibility handlers
        initiated = new BaseKafkaConsumer<>(PaymentInitiatedEvent.class, deserializer) {
            @Override
            protected void handle(PaymentInitiatedEvent e, ConsumerRecord<String, PaymentInitiatedEvent> rec) {
                log.debug("PaymentInitiated received: orderId={}, paymentId={}, offset={}", e.getOrderId(), e.getPaymentId(), rec.offset());
                handler.onPaymentInitiated(e.getOrderId(), e.getPaymentId());
            }
        };

        processed = new BaseKafkaConsumer<>(PaymentProcessedEvent.class, deserializer) {
            @Override
            protected void handle(PaymentProcessedEvent e, ConsumerRecord<String, PaymentProcessedEvent> rec) {
                log.debug("PaymentProcessed received: orderId={}, paymentId={}, txRef={}, offset={}", e.getOrderId(), e.getPaymentId(), e.getTransactionReference(), rec.offset());
                handler.onPaymentProcessed(e.getOrderId(), e.getPaymentId(), e.getAmount(), e.getCurrency(), e.getTransactionReference());
            }
        };

        failed = new BaseKafkaConsumer<>(PaymentFailedEvent.class, deserializer) {
            @Override
            protected void handle(PaymentFailedEvent e, ConsumerRecord<String, PaymentFailedEvent> rec) {
                log.warn("PaymentFailed received: orderId={}, paymentId={}, reason={}, offset={}", e.getOrderId(), e.getPaymentId(), e.getFailureReason(), rec.offset());
                handler.onPaymentFailed(e.getOrderId(), e.getPaymentId(), e.getFailureReason());
            }
        };

        refunded = new BaseKafkaConsumer<>(RefundIssuedEvent.class, deserializer) {
            @Override
            protected void handle(RefundIssuedEvent e, ConsumerRecord<String, RefundIssuedEvent> rec) {
                log.debug("RefundIssued received: orderId={}, paymentId={}, amount={}, currency={}, offset={}", e.getOrderId(), e.getPaymentId(), e.getAmount(), e.getCurrency(), rec.offset());
                handler.onRefundIssued(e.getOrderId(), e.getPaymentId(), e.getAmount(), e.getCurrency());
            }
        };
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.payments.initiated}",
            groupId = "orders-payments"
    )
    public void onInitiated(ConsumerRecord<String, PaymentInitiatedEvent> rec) {
        initiated.consume(rec);
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.payments.processed}",
            groupId = "orders-payments"
    )
    public void onProcessed(ConsumerRecord<String, PaymentProcessedEvent> rec) {
        processed.consume(rec);
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.payments.failed}",
            groupId = "orders-payments"
    )
    public void onFailed(ConsumerRecord<String, PaymentFailedEvent> rec) {
        failed.consume(rec);
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.payments.refund-issued}",
            groupId = "orders-payments"
    )
    public void onRefunded(ConsumerRecord<String, RefundIssuedEvent> rec) {
        refunded.consume(rec);
    }
}
