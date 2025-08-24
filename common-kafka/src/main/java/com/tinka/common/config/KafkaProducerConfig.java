package com.tinka.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    @ConditionalOnMissingBean
    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProps,
                                                           ObjectMapper objectMapper) {
        // Start from Spring Boot's merged properties (includes bootstrap-servers and your producer.*)
        Map<String, Object> conf = new HashMap<>(kafkaProps.buildProducerProperties());

        // Ensure serializers (keeps it explicit even if already present from shared config)
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonSerializer.class);

        // Align with your shared config choice: type headers ON (recommended)
        conf.put("spring.json.add.type.headers", true);

        return new DefaultKafkaProducerFactory<>(conf);
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        KafkaTemplate<String, Object> kt = new KafkaTemplate<>(pf);
        // Enable Micrometer metrics/tracing if present
        kt.setObservationEnabled(true);
        return kt;
    }
}
