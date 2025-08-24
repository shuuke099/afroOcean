package com.tinka.orders.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.auth.SellerVerifiedEvent;
import com.tinka.common.events.auth.UserCreatedEvent;
import com.tinka.common.events.auth.UserDeletedEvent;
import com.tinka.common.events.auth.UserUpdatedEvent;
import com.tinka.orders.service.OrderConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class AuthEventsConsumer {

    private final BaseKafkaConsumer<UserCreatedEvent> userCreated;
    private final BaseKafkaConsumer<UserUpdatedEvent> userUpdated;
    private final BaseKafkaConsumer<UserDeletedEvent> userDeleted;
    private final BaseKafkaConsumer<SellerVerifiedEvent> sellerVerified;

    public AuthEventsConsumer(EventDeserializer des, OrderConsumerHandler handler) {
        this.userCreated = new BaseKafkaConsumer<>(UserCreatedEvent.class, des) {
            @Override protected void handle(UserCreatedEvent e, ConsumerRecord<String, UserCreatedEvent> rec) {
                // UserCreatedEvent: userId, fullName, email, role, createdAt
                handler.onUserCreated(e.getUserId(), e.getFullName(), e.getEmail(), e.getRole(), e.getCreatedAt());
            }
        };

        this.userUpdated = new BaseKafkaConsumer<>(UserUpdatedEvent.class, des) {
            @Override protected void handle(UserUpdatedEvent e, ConsumerRecord<String, UserUpdatedEvent> rec) {
                // Adjust field names if your DTO differs (e.g., fullName vs first/last, updatedAt vs occurredAt)
                handler.onUserUpdated(e.getUserId(), e.getFullName(), e.getEmail(), e.getRole(), e.getUpdatedAt());
            }
        };

        this.userDeleted = new BaseKafkaConsumer<>(UserDeletedEvent.class, des) {
            @Override protected void handle(UserDeletedEvent e, ConsumerRecord<String, UserDeletedEvent> rec) {
                // If your DTO uses deletedAt/occurredAt/timestamp, swap accordingly
                handler.onUserDeleted(e.getUserId(), e.getDeletedAt());
            }
        };

        this.sellerVerified = new BaseKafkaConsumer<>(SellerVerifiedEvent.class, des) {
            @Override protected void handle(SellerVerifiedEvent e, ConsumerRecord<String, SellerVerifiedEvent> rec) {
                String sellerId = e.getSellerId() != null
                        ? e.getSellerId().toString()
                        : (e.getUserId() != null ? e.getUserId() : null);

                handler.onSellerVerified(sellerId, e.getVerifiedAt());
            }
        };


    }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.auth.user-created}", groupId = "orders-auth")
    public void onUserCreated(ConsumerRecord<String, UserCreatedEvent> rec) { userCreated.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.auth.user-updated}", groupId = "orders-auth")
    public void onUserUpdated(ConsumerRecord<String, UserUpdatedEvent> rec) { userUpdated.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.auth.user-deleted}", groupId = "orders-auth")
    public void onUserDeleted(ConsumerRecord<String, UserDeletedEvent> rec) { userDeleted.consume(rec); }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.auth.seller-verified}", groupId = "orders-auth")
    public void onSellerVerified(ConsumerRecord<String, SellerVerifiedEvent> rec) { sellerVerified.consume(rec); }

}
