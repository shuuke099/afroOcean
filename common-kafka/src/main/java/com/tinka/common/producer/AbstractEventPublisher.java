package com.tinka.common.producer;

import com.tinka.common.config.TopicProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractEventPublisher extends BaseKafkaProducer implements EventPublisher<Object> {

    private final TopicProperties topics;

    protected AbstractEventPublisher(KafkaTemplate<String, Object> kafka, TopicProperties topics) {
        super(kafka);
        this.topics = topics;
    }

    @Override
    public CompletableFuture<?> publish(String topic, String key, Object event) {
        return send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event {} to topic {}", event, topic, ex);
                    } else {
                        log.debug("Successfully published event {} to topic {}", event, topic);
                    }
                });
    }

    protected static String key(Object id) {
        return Objects.toString(id, "");
    }

    protected TopicProperties topics() {
        return topics;
    }
}
