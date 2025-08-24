// src/main/java/com/tinka/auth/kafka/AuthEventPublisher.java
package com.tinka.auth.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.producer.BaseKafkaProducer;
import com.tinka.common.events.auth.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AuthEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public AuthEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    public CompletableFuture<?> userCreated(UserCreatedEvent e) {
        return logOnResult("userCreated", e, send(topics.getAuth().getUserCreated(), key(e.getUserId()), e));
    }

    public CompletableFuture<?> userUpdated(UserUpdatedEvent e) {
        return logOnResult("userUpdated", e, send(topics.getAuth().getUserUpdated(), key(e.getUserId()), e));
    }

    public CompletableFuture<?> userDeleted(UserDeletedEvent e) {
        return logOnResult("userDeleted", e, send(topics.getAuth().getUserDeleted(), key(e.getUserId()), e));
    }

    public CompletableFuture<?> sellerVerified(SellerVerifiedEvent e) {
        return logOnResult("sellerVerified", e, send(topics.getAuth().getSellerVerified(), key(e.getUserId()), e));
    }

    private static String key(Object id) {
        return Objects.toString(id, "");
    }

    // DRY helper for consistent logging
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
