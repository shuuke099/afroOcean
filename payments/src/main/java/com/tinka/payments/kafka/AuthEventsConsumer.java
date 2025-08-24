// payments/src/main/java/com/tinka/payments/kafka/AuthEventsConsumer.java
package com.tinka.payments.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.auth.SellerVerifiedEvent;
import com.tinka.common.events.auth.UserDeletedEvent;
import com.tinka.payments.service.PaymentConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class AuthEventsConsumer {

    private final BaseKafkaConsumer<SellerVerifiedEvent> sellerVerified;
    private final BaseKafkaConsumer<UserDeletedEvent> userDeleted;

    public AuthEventsConsumer(ObjectMapper mapper, PaymentConsumerHandler handler) {
        // build the EventDeserializer using Boot’s configured ObjectMapper
        EventDeserializer des = new EventDeserializer(mapper);

        this.sellerVerified = new BaseKafkaConsumer<>(SellerVerifiedEvent.class, des) {
            @Override
            protected void handle(SellerVerifiedEvent e, ConsumerRecord<String, SellerVerifiedEvent> rec) {
                handler.onSellerVerified(e);
            }
        };
        this.userDeleted = new BaseKafkaConsumer<>(UserDeletedEvent.class, des) {
            @Override
            protected void handle(UserDeletedEvent e, ConsumerRecord<String, UserDeletedEvent> rec) {
                handler.onUserDeleted(e);
            }
        };
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.auth.seller-verified}",
            groupId = "payments-auth"
    )
    public void onSellerVerified(ConsumerRecord<String, SellerVerifiedEvent> rec) {
        sellerVerified.consume(rec);
    }

    @MarketplaceKafkaListener(
            topics = "${tinka.kafka.topics.auth.user-deleted}",
            groupId = "payments-auth"
    )
    public void onUserDeleted(ConsumerRecord<String, UserDeletedEvent> rec) {
        userDeleted.consume(rec);
    }
}
