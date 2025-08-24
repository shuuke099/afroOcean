package com.tinka.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.annotation.*;

/**
 * Centralized Kafka listener for the Tinka Marketplace.
 *
 * This wraps {@link KafkaListener} and applies common conventions:
 * - Group ID can default to service name (set in application.yml)
 * - Topic placeholders are supported (e.g., "${topics.orders.placed}")
 * - Avoids repeating boilerplate across services
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@KafkaListener
public @interface MarketplaceKafkaListener {

    /**
     * Alias for {@link KafkaListener#topics()}.
     * Use placeholder style: "${topics.orders.placed}".
     */
    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics() default {};

    /**
     * Alias for {@link KafkaListener#groupId()}.
     * Defaults to "${spring.application.name}" unless overridden.
     */
    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "${spring.application.name}";

    /**
     * Alias for {@link KafkaListener#containerFactory()}.
     * This allows switching between different factories (e.g., batch or JSON).
     */
    @AliasFor(annotation = KafkaListener.class, attribute = "containerFactory")
    String containerFactory() default "";
}
