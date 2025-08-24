package com.tinka.notification.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.events.notification.NotificationRequestedEvent; // <-- plural 'notifications'
import com.tinka.common.producer.BaseKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class NotificationEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public NotificationEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = Objects.requireNonNull(topics, "TopicProperties must not be null");
    }

    /** Publish when any service requests a notification (email/SMS/push). */
    public CompletableFuture<?> notificationRequested(NotificationRequestedEvent e) {
        String topic = require(topics.getNotification().getRequested(), "topics.notifications.requested");
        return logOnResult(
                "notificationRequested",
                e,
                send(topic, key(e), e)
        );
    }

    // ---- Helpers ----
    private static String key(NotificationRequestedEvent e) {
        // Prefer recipientId, then userId, then channel, else empty
        if (notBlank(e.getRecipientId())) return e.getRecipientId();
        if (notBlank(e.getUserId()))      return e.getUserId();
        if (notBlank(e.getChannel()))     return e.getChannel();
        return "";
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String require(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing Kafka topic config: " + name);
        }
        return value;
    }

    private <T> CompletableFuture<T> logOnResult(String eventType, Object event, CompletableFuture<T> future) {
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish {}: {}", eventType, event, ex);
            } else {
                log.debug("Successfully published {}: {}", eventType, event);
            }
        });
        return future;
    }
}
