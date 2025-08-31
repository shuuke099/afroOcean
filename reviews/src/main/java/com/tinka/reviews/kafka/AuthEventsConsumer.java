package com.tinka.reviews.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.auth.UserDeletedEvent;
import com.tinka.reviews.service.ReviewConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class AuthEventsConsumer {

    private final BaseKafkaConsumer<UserDeletedEvent> userDeleted;

    public AuthEventsConsumer(EventDeserializer des, ReviewConsumerHandler handler) {
        this.userDeleted = new BaseKafkaConsumer<>(UserDeletedEvent.class, des) {
            @Override protected void handle(UserDeletedEvent e, ConsumerRecord<String, UserDeletedEvent> rec) {
                handler.onUserDeleted(e);
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${tinka.kafka.topics.auth.user-deleted}", groupId = "reviews-auth")
    public void onUserDeleted(ConsumerRecord<String, UserDeletedEvent> rec) { userDeleted.consume(rec); }
}
