package com.tinka.orders.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.producer.BaseKafkaProducer;
import com.tinka.common.events.orders.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OrderEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    public CompletableFuture<?> orderPlaced(OrderPlacedEvent e) {
        return logOnResult("orderPlaced", e, send(topics.getOrders().getPlaced(), key(e.getOrderId()), e));
    }

    public CompletableFuture<?> orderConfirmed(OrderConfirmedEvent e) {
        return logOnResult("orderConfirmed", e, send(topics.getOrders().getConfirmed(), key(e.getOrderId()), e));
    }

    public CompletableFuture<?> orderCancelled(OrderCancelledEvent e) {
        return logOnResult("orderCancelled", e, send(topics.getOrders().getCancelled(), key(e.getOrderId()), e));
    }

    public CompletableFuture<?> orderShipped(OrderShippedEvent e) {
        return logOnResult("orderShipped", e, send(topics.getOrders().getShipped(), key(e.getOrderId()), e));
    }

    public CompletableFuture<?> orderDelivered(OrderDeliveredEvent e) {
        return logOnResult("orderDelivered", e, send(topics.getOrders().getDelivered(), key(e.getOrderId()), e));
    }

    public CompletableFuture<?> orderFailed(OrderFailedEvent e) {
        return logOnResult("orderFailed", e, send(topics.getOrders().getFailed(), key(e.getOrderId()), e));
    }

    // --- Helper for partition key ---
    private static String key(Object id) {
        return Objects.toString(id, "");
    }

    // --- Helper for consistent logging ---
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
