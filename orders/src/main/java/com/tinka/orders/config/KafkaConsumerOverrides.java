package com.tinka.orders.config;

import com.tinka.common.consumer.EventDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerOverrides {
    @Bean
    public EventDeserializer eventDeserializer() {
        return new EventDeserializer();
    }
}
