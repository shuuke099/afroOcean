package com.tinka.products.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.producer.BaseKafkaProducer;
import com.tinka.common.events.products.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProductEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public ProductEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    public CompletableFuture<?> productCreated(ProductCreatedEvent e) {
        return logOnResult("productCreated", e, send(topics.getProducts().getCreated(), key(e.getProductId()), e));
    }

    public CompletableFuture<?> productUpdated(ProductUpdatedEvent e) {
        return logOnResult("productUpdated", e, send(topics.getProducts().getUpdated(), key(e.getProductId()), e));
    }

    public CompletableFuture<?> productDeleted(ProductDeletedEvent e) {
        return logOnResult("productDeleted", e, send(topics.getProducts().getDeleted(), key(e.getProductId()), e));
    }

    public CompletableFuture<?> productVerified(ProductVerifiedEvent e) {
        return logOnResult("productVerified", e, send(topics.getProducts().getVerified(), key(e.getProductId()), e));
    }

    public CompletableFuture<?> productOutOfStock(ProductOutOfStockEvent e) {
        return logOnResult("productOutOfStock", e, send(topics.getProducts().getOutOfStock(), key(e.getProductId()), e));
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
