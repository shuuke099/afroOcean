package com.tinka.reviews.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.products.ProductDeletedEvent;
import com.tinka.common.events.products.ProductVerifiedEvent;
import com.tinka.reviews.service.ReviewConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsConsumer {

    private final BaseKafkaConsumer<ProductDeletedEvent> deleted;
    private final BaseKafkaConsumer<ProductVerifiedEvent> verified;

    public ProductEventsConsumer(EventDeserializer des, ReviewConsumerHandler handler) {
        this.deleted = new BaseKafkaConsumer<>(ProductDeletedEvent.class, des) {
            @Override protected void handle(ProductDeletedEvent e, ConsumerRecord<String, ProductDeletedEvent> rec) {
                handler.onProductDeleted(e);
            }
        };
        this.verified = new BaseKafkaConsumer<>(ProductVerifiedEvent.class, des) {
            @Override protected void handle(ProductVerifiedEvent e, ConsumerRecord<String, ProductVerifiedEvent> rec) {
                handler.onProductVerified(e);
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${topics.products.deleted}", groupId = "reviews-products")
    public void onDeleted(ConsumerRecord<String, ProductDeletedEvent> rec) { deleted.consume(rec); }

    @MarketplaceKafkaListener(topics = "${topics.products.verified}", groupId = "reviews-products")
    public void onVerified(ConsumerRecord<String, ProductVerifiedEvent> rec) { verified.consume(rec); }
}
