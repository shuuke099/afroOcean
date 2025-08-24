package com.tinka.orders.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.products.*;
import com.tinka.orders.service.OrderConsumerHandler;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ProductEventsConsumer {

    // Inject only these via Lombok-generated constructor:
    private final EventDeserializer des;
    private final OrderConsumerHandler handler;

    // Create these after injection (not final; set in @PostConstruct):
    private BaseKafkaConsumer<ProductCreatedEvent> created;
    private BaseKafkaConsumer<ProductUpdatedEvent> updated;
    private BaseKafkaConsumer<ProductDeletedEvent> deleted;
    private BaseKafkaConsumer<ProductVerifiedEvent> verified;
    private BaseKafkaConsumer<ProductOutOfStockEvent> outOfStock;

    @PostConstruct
    void init() {
        this.created = new BaseKafkaConsumer<>(ProductCreatedEvent.class, des) {
            @Override protected void handle(ProductCreatedEvent e, ConsumerRecord<String, ProductCreatedEvent> rec) {
                handler.onProductCreated(e.getProductId());
            }
        };
        this.updated = new BaseKafkaConsumer<>(ProductUpdatedEvent.class, des) {
            @Override protected void handle(ProductUpdatedEvent e, ConsumerRecord<String, ProductUpdatedEvent> rec) {
                handler.onProductUpdated(e.getProductId());
            }
        };
        this.deleted = new BaseKafkaConsumer<>(ProductDeletedEvent.class, des) {
            @Override protected void handle(ProductDeletedEvent e, ConsumerRecord<String, ProductDeletedEvent> rec) {
                handler.onProductDeleted(e.getProductId());
            }
        };
        this.verified = new BaseKafkaConsumer<>(ProductVerifiedEvent.class, des) {
            @Override protected void handle(ProductVerifiedEvent e, ConsumerRecord<String, ProductVerifiedEvent> rec) {
                handler.onProductVerified(e.getProductId());
            }
        };
        this.outOfStock = new BaseKafkaConsumer<>(ProductOutOfStockEvent.class, des) {
            @Override protected void handle(ProductOutOfStockEvent e, ConsumerRecord<String, ProductOutOfStockEvent> rec) {
                handler.onProductOutOfStock(e.getProductId());
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.products.created}")
    public void onCreated(ConsumerRecord<String, ProductCreatedEvent> rec) { created.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.products.updated}")
    public void onUpdated(ConsumerRecord<String, ProductUpdatedEvent> rec) { updated.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.products.deleted}")
    public void onDeleted(ConsumerRecord<String, ProductDeletedEvent> rec) { deleted.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.products.verified}")
    public void onVerified(ConsumerRecord<String, ProductVerifiedEvent> rec) { verified.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.products.out-of-stock}")
    public void onOutOfStock(ConsumerRecord<String, ProductOutOfStockEvent> rec) { outOfStock.consume(rec); }
}
