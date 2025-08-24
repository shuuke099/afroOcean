package com.tinka.orders.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.orders.service.OrderConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventsConsumer {

    private final BaseKafkaConsumer<ReviewCreatedEvent> reviewCreated;

    // Constructor injection only
    public ReviewEventsConsumer(EventDeserializer des, OrderConsumerHandler handler) {
        this.reviewCreated = new BaseKafkaConsumer<>(ReviewCreatedEvent.class, des) {
            @Override
            protected void handle(ReviewCreatedEvent e, ConsumerRecord<String, ReviewCreatedEvent> rec) {
                handler.onReviewCreated(
                        e.getProductId(),
                        e.getReviewId(),
                        e.getUserId(),
                        e.getRating(),
                        e.getComment()
                );
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.reviews.created}")
    public void onReviewCreated(ConsumerRecord<String, ReviewCreatedEvent> rec) {
        reviewCreated.consume(rec);
    }
}
