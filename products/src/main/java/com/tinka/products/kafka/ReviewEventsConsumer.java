package com.tinka.products.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.reviews.ReviewCreatedEvent;
import com.tinka.products.service.ProductConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventsConsumer {

    private final BaseKafkaConsumer<ReviewCreatedEvent> reviewCreated;

    public ReviewEventsConsumer(EventDeserializer des, ProductConsumerHandler handler) {
        this.reviewCreated = new BaseKafkaConsumer<>(ReviewCreatedEvent.class, des) {
            @Override protected void handle(ReviewCreatedEvent e, ConsumerRecord<String, ReviewCreatedEvent> rec) {
                handler.onReviewCreated(e);
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${topics.reviews.created}", groupId = "products-reviews")
    public void onReviewCreated(ConsumerRecord<String, ReviewCreatedEvent> rec) { reviewCreated.consume(rec); }

}
