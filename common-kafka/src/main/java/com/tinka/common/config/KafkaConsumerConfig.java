package com.tinka.common.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    @ConditionalOnMissingBean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProps) {
        // Start from Spring Boot's merged consumer properties (bootstrap, group-id, etc.)
        Map<String, Object> conf = new HashMap<>(kafkaProps.buildConsumerProperties());

        // Ensure deserializers (explicit even if already present in config)
        conf.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        conf.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Trust your shared events package; type headers ON to auto-map POJOs
        conf.put(JsonDeserializer.TRUSTED_PACKAGES, "com.tinka.common.events.*");
        conf.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, true);

        return new DefaultKafkaConsumerFactory<>(conf);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            // Optional; if you defined a DefaultErrorHandler/DeadLetterPublishingRecoverer bean, it will be injected here
            CommonErrorHandler commonErrorHandler,
            KafkaProperties kafkaProps
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);

        // Error handling & retries/DLT (if a bean is present)
        if (commonErrorHandler != null) {
            factory.setCommonErrorHandler(commonErrorHandler);
        }

        // Acks & topics behavior
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.getContainerProperties().setMissingTopicsFatal(false);

        // Observability (Micrometer metrics/tracing)
        factory.getContainerProperties().setObservationEnabled(true);

        // Respect configured concurrency (fallback to 3 if not set)
        Integer concurrency = kafkaProps.getListener().getConcurrency();
        factory.setConcurrency(concurrency != null ? concurrency : 3);

        return factory;
    }
}
