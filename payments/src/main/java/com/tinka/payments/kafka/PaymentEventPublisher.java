package com.tinka.payments.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.producer.BaseKafkaProducer;
import com.tinka.common.events.payments.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PaymentEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public PaymentEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    public CompletableFuture<?> paymentInitiated(PaymentInitiatedEvent e) {
        return logOnResult("paymentInitiated", e, send(topics.getPayments().getInitiated(), key(e.getPaymentId()), e));
    }

    public CompletableFuture<?> paymentProcessed(PaymentProcessedEvent e) {
        return logOnResult("paymentProcessed", e, send(topics.getPayments().getProcessed(), key(e.getPaymentId()), e));
    }

    public CompletableFuture<?> paymentFailed(PaymentFailedEvent e) {
        return logOnResult("paymentFailed", e, send(topics.getPayments().getFailed(), key(e.getPaymentId()), e));
    }

    public CompletableFuture<?> refundIssued(RefundIssuedEvent e) {
        return logOnResult("refundIssued", e, send(topics.getPayments().getRefundIssued(), key(e.getPaymentId()), e));
    }

    // --- Partition key for Kafka ---
    private static String key(Object id) {
        return Objects.toString(id, "");
    }

    // --- Logging for all futures ---
    private <T> CompletableFuture<T> logOnResult(String eventType, Object event, CompletableFuture<T> future) {
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish {} event: {}", eventType, event, ex);
            } else {
                log.debug("Successfully published {} event: {}", eventType, event);
            }
        });
        return future;
    }
}
