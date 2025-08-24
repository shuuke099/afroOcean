package com.tinka.common.consumer;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for Kafka consumers to standardize logging, (optional) deserialization,
 * and the lifecycle around handling events.
 *
 * Usage:
 * 1) Typed listener (Spring Kafka already deserializes to T):
 *    {@code
 *    @Component
 *    public class ReviewCreatedConsumer extends BaseKafkaConsumer<ReviewCreatedEvent> {
 *      public ReviewCreatedConsumer(EventDeserializer des) { super(ReviewCreatedEvent.class, des); }
 *
 *      @KafkaListener(topics="${topics.reviews.created}", groupId="reviews")
 *      public void onMessage(ConsumerRecord<String, ReviewCreatedEvent> rec) { consume(rec); }
 *
 *      @Override protected void handle(ReviewCreatedEvent e, ConsumerRecord<String, ReviewCreatedEvent> rec) { ... }
 *    }}
 *
 * 2) Raw listener (manual deserialization via EventDeserializer):
 *    {@code
 *    @Component
 *    public class OrderPlacedConsumer extends BaseKafkaConsumer<OrderPlacedEvent> {
 *      public OrderPlacedConsumer(EventDeserializer des) { super(OrderPlacedEvent.class, des); }
 *
 *      @KafkaListener(topics="${topics.orders.placed}", groupId="orders")
 *      public void onMessage(ConsumerRecord<String, String> rec) { consumeRaw(rec); }
 *
 *      @Override protected void handle(OrderPlacedEvent e, ConsumerRecord<String, OrderPlacedEvent> rec) { ... }
 *    }}
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class BaseKafkaConsumer<T> {

    /** Target event type this consumer handles. */
    @NonNull private final Class<T> eventType;

    /** Shared JSON mapper utility for raw listeners. Safe defaults (JavaTime, ignore unknowns). */
    @NonNull private final EventDeserializer deserializer;

    // ===== Entry points =====

    /** Use when your @KafkaListener receives a typed record: ConsumerRecord&lt;String, T&gt;. */
    public final void consume(ConsumerRecord<String, T> record) {
        if (record == null) {
            log.warn("Null ConsumerRecord for {}", safeType());
            return;
        }
        T payload = record.value();
        logReceived(record, payload);
        pipeline(record, payload);
    }

    /** Use when your @KafkaListener receives raw String payloads. */
    public final void consumeRaw(ConsumerRecord<String, String> record) {
        if (record == null) {
            log.warn("Null ConsumerRecord<String,String> for {}", safeType());
            return;
        }
        T payload = deserializer.fromJson(record.value(), eventType);
        logReceivedRaw(record);
        pipeline(castRecord(record, payload), payload);
    }

    /** Use when your @KafkaListener receives raw byte[] payloads. */
    public final void consumeRawBytes(ConsumerRecord<String, byte[]> record) {
        if (record == null) {
            log.warn("Null ConsumerRecord<String,byte[]> for {}", safeType());
            return;
        }
        T payload = deserializer.fromBytes(record.value(), eventType);
        logReceivedRaw(record);
        pipeline(castRecord(record, payload), payload);
    }

    // ===== Template pipeline =====

    private void pipeline(ConsumerRecord<String, T> record, T payload) {
        try {
            beforeHandle(record, payload);
            handle(payload, record);
            afterHandle(record, payload);
        } catch (Exception ex) {
            onError(record, payload, ex);
        }
    }

    /** Implement business logic here. Throwing will route to onError(...). */
    protected abstract void handle(T event, ConsumerRecord<String, T> record) throws Exception;

    /** Hook: runs before handle(...). Override if needed. */
    protected void beforeHandle(ConsumerRecord<String, T> record, T payload) { /* no-op */ }

    /** Hook: runs after handle(...). Override if needed. */
    protected void afterHandle(ConsumerRecord<String, T> record, T payload) { /* no-op */ }

    /** Hook: error handling. Override to add metrics/DLQ routing if desired. */
    protected void onError(ConsumerRecord<String, T> record, T payload, Exception ex) {
        log.error(
                "Error handling event type={} topic={} key={} partition={} offset={} headers={} payload={}",
                safeType(), record.topic(), record.key(), record.partition(), record.offset(),
                headersToMap(record), payload, ex
        );
    }

    // ===== Logging helpers =====

    protected void logReceived(ConsumerRecord<String, T> record, T payload) {
        log.info(
                "Consumed type={} topic={} key={} partition={} offset={} ts={} headers={}",
                safeType(), record.topic(), record.key(), record.partition(), record.offset(),
                Instant.ofEpochMilli(record.timestamp()), headersToMap(record)
        );
        if (log.isDebugEnabled()) {
            log.debug("Payload: {}", payload);
        }
    }

    protected void logReceivedRaw(ConsumerRecord<?, ?> record) {
        log.info(
                "Consumed RAW type={} topic={} key={} partition={} offset={} ts={} headers={}",
                safeType(), record.topic(), record.key(), record.partition(), record.offset(),
                Instant.ofEpochMilli(record.timestamp()), headersToMap(record)
        );
    }

    protected Map<String, String> headersToMap(ConsumerRecord<?, ?> record) {
        Map<String, String> map = new HashMap<>();
        for (Header h : record.headers()) {
            map.put(h.key(), h.value() == null ? null : new String(h.value(), StandardCharsets.UTF_8));
        }
        return map;
    }

    protected String header(ConsumerRecord<?, ?> record, String key) {
        Header h = record.headers().lastHeader(key);
        return h == null ? null : new String(h.value(), StandardCharsets.UTF_8);
    }

    private String safeType() {
        return eventType.getSimpleName();
    }

    /** Create a typed record view for downstream hooks/logs when starting from a raw record. */
    private ConsumerRecord<String, T> castRecord(ConsumerRecord<?, ?> source, T payload) {
        return new ConsumerRecord<>(
                source.topic(),
                source.partition(),
                source.offset(),
                (String) source.key(),
                payload
        );
    }
}
