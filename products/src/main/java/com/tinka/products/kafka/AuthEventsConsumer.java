package com.tinka.products.kafka;

import com.tinka.common.annotation.MarketplaceKafkaListener;
import com.tinka.common.consumer.BaseKafkaConsumer;
import com.tinka.common.consumer.EventDeserializer;
import com.tinka.common.events.auth.SellerVerifiedEvent;
import com.tinka.products.service.ProductConsumerHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class AuthEventsConsumer {

    private final BaseKafkaConsumer<SellerVerifiedEvent> sellerVerified;

    public AuthEventsConsumer(EventDeserializer des, ProductConsumerHandler handler) {
        this.sellerVerified = new BaseKafkaConsumer<>(SellerVerifiedEvent.class, des) {
            @Override protected void handle(SellerVerifiedEvent e, ConsumerRecord<String, SellerVerifiedEvent> rec) {
                handler.onSellerVerified(e);
            }
        };
    }

    @MarketplaceKafkaListener(topics = "${topics.auth.seller-verified}", groupId = "products-auth")
    public void onSellerVerified(ConsumerRecord<String, SellerVerifiedEvent> rec) { sellerVerified.consume(rec); }
}
