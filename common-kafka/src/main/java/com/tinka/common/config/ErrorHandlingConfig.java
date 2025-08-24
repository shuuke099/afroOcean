package com.tinka.common.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class ErrorHandlingConfig {

    private final KafkaTemplate<String, Object> template;

    @Bean
    @ConditionalOnMissingBean
    public DeadLetterPublishingRecoverer dltRecoverer() {
        return new DeadLetterPublishingRecoverer(template, (ConsumerRecord<?, ?> rec, Exception ex) -> {
            String dlt = rec.topic() + ".DLT";
            return new org.apache.kafka.common.TopicPartition(dlt, rec.partition());
        });
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultErrorHandler commonErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        var backoff = new FixedBackOff(1000L, 3); // 3 retries, 1s apart
        return new DefaultErrorHandler(recoverer, backoff);
    }
}
