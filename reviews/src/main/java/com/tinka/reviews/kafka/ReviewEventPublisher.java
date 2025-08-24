package com.tinka.reviews.kafka;

import com.tinka.common.config.TopicProperties;
import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.common.producer.BaseKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ReviewEventPublisher extends BaseKafkaProducer {

    private final TopicProperties topics;

    public ReviewEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    public CompletableFuture<?> reviewCreated(ReviewCreatedEvent e) {
        String topic = topics.getReviews().getCreated();
        if (topic == null || topic.isBlank()) {
            throw new IllegalStateException("Missing Kafka topic config: topics.reviews.created");
        }
        String key = key(e);
        return send(topic, key, e).whenComplete((ok, ex) -> {
            if (ex != null) {
                log.error("Failed to publish reviewCreated event: {}", e, ex);
            } else {
                log.debug("Successfully published reviewCreated event: {}", e);
            }
        });
    }

    private static String key(ReviewCreatedEvent e) {
        // prefer reviewId -> orderId -> sellerId -> userId -> ""
        if (notBlank(e.getReviewId())) return e.getReviewId();
        if (notBlank(e.getOrderId()))  return e.getOrderId();
        if (notBlank(e.getSellerId())) return e.getSellerId();
        if (notBlank(e.getUserId()))   return e.getUserId();
        return "";
    }

    private static boolean notBlank(String s) { return s != null && !s.isBlank(); }
}
