package com.tinka.reviews.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.orders.*;
import com.tinka.reviews.service.ReviewConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsConsumer {

    private final BaseKafkaConsumer<OrderDeliveredEvent> delivered;
    private final BaseKafkaConsumer<OrderCancelledEvent> cancelled;
    private final BaseKafkaConsumer<OrderFailedEvent> failed;

    public OrderEventsConsumer(EventDeserializer des, ReviewConsumerHandler handler) {
        this.delivered = new BaseKafkaConsumer<>(OrderDeliveredEvent.class, des) {
            @Override protected void handle(OrderDeliveredEvent e, ConsumerRecord<String, OrderDeliveredEvent> rec) {
                handler.onOrderDelivered(e);
            }
        };
        this.cancelled = new BaseKafkaConsumer<>(OrderCancelledEvent.class, des) {
            @Override protected void handle(OrderCancelledEvent e, ConsumerRecord<String, OrderCancelledEvent> rec) {
                handler.onOrderCancelled(e);
            }
        };
        this.failed = new BaseKafkaConsumer<>(OrderFailedEvent.class, des) {
            @Override protected void handle(OrderFailedEvent e, ConsumerRecord<String, OrderFailedEvent> rec) {
                handler.onOrderFailed(e);
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.orders.delivered}", groupId = "reviews-orders")
    public void onDelivered(ConsumerRecord<String, OrderDeliveredEvent> rec) { delivered.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.orders.cancelled}", groupId = "reviews-orders")
    public void onCancelled(ConsumerRecord<String, OrderCancelledEvent> rec) { cancelled.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.orders.failed}", groupId = "reviews-orders")
    public void onFailed(ConsumerRecord<String, OrderFailedEvent> rec) { failed.consume(rec); }
}
