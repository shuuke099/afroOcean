package com.tinka.products.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.orders.*;
import com.tinka.products.service.ProductConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsConsumer {

    private final BaseKafkaConsumer<OrderPlacedEvent> placed;
    private final BaseKafkaConsumer<OrderCancelledEvent> cancelled;
    private final BaseKafkaConsumer<OrderFailedEvent> failed;
    private final BaseKafkaConsumer<OrderShippedEvent> shipped;
    private final BaseKafkaConsumer<OrderConfirmedEvent> confirmed;

    public OrderEventsConsumer(EventDeserializer des, ProductConsumerHandler handler) {
        this.placed = new BaseKafkaConsumer<>(OrderPlacedEvent.class, des) {
            @Override protected void handle(OrderPlacedEvent e, ConsumerRecord<String, OrderPlacedEvent> rec) {
                handler.onOrderPlaced(e); // reserve
            }
        };
        this.cancelled = new BaseKafkaConsumer<>(OrderCancelledEvent.class, des) {
            @Override protected void handle(OrderCancelledEvent e, ConsumerRecord<String, OrderCancelledEvent> rec) {
                handler.onOrderCancelled(e); // release
            }
        };
        this.failed = new BaseKafkaConsumer<>(OrderFailedEvent.class, des) {
            @Override protected void handle(OrderFailedEvent e, ConsumerRecord<String, OrderFailedEvent> rec) {
                handler.onOrderFailed(e); // release
            }
        };
        this.shipped = new BaseKafkaConsumer<>(OrderShippedEvent.class, des) {
            @Override protected void handle(OrderShippedEvent e, ConsumerRecord<String, OrderShippedEvent> rec) {
                handler.onOrderShipped(e); // commit
            }
        };
        this.confirmed = new BaseKafkaConsumer<>(OrderConfirmedEvent.class, des) {
            @Override protected void handle(OrderConfirmedEvent e, ConsumerRecord<String, OrderConfirmedEvent> rec) {
                handler.onOrderConfirmed(e); // optional
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${topics.orders.placed}", groupId = "products-orders")
    public void onPlaced(ConsumerRecord<String, OrderPlacedEvent> rec) { placed.consume(rec); }

    @MarketplaceKafkaListener(topics = "${topics.orders.cancelled}", groupId = "products-orders")
    public void onCancelled(ConsumerRecord<String, OrderCancelledEvent> rec) { cancelled.consume(rec); }

    @MarketplaceKafkaListener(topics = "${topics.orders.failed}", groupId = "products-orders")
    public void onFailed(ConsumerRecord<String, OrderFailedEvent> rec) { failed.consume(rec); }

    @MarketplaceKafkaListener(topics = "${topics.orders.shipped}", groupId = "products-orders")
    public void onShipped(ConsumerRecord<String, OrderShippedEvent> rec) { shipped.consume(rec); }

    @MarketplaceKafkaListener(topics = "${topics.orders.confirmed}", groupId = "products-orders")
    public void onConfirmed(ConsumerRecord<String, OrderConfirmedEvent> rec) { confirmed.consume(rec); }
}
