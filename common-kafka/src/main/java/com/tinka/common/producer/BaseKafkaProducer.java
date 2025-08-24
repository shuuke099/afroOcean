package com.tinka.common.producer;

import com.tinka.common.events.EventEnvelope;
import com.tinka.common.config.KafkaHeadersEx;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Base producer with sane defaults:
 * - JSON value serializer (as configured in KafkaProducerConfig)
 * - Micrometer observation enabled via KafkaTemplate
 * - Simple helpers for headers and envelopes
 */
@RequiredArgsConstructor
public abstract class BaseKafkaProducer {

    protected final KafkaTemplate<String, Object> kafka;

    /** Fire-and-forget with key (recommended for ordering by aggregate). */
    protected CompletableFuture<SendResult<String, Object>> send(String topic, String key, Object payload) {
        return kafka.send(topic, key, payload);
    }

    /** Send with custom headers (e.g., correlation/causation/tenant). */
    protected CompletableFuture<SendResult<String, Object>> send(
            String topic, String key, Object payload, Map<String, String> headers) {

        ProducerRecord<String, Object> rec = new ProducerRecord<>(topic, null, key, payload);
        if (headers != null) {
            headers.forEach((k, v) ->
                    rec.headers().add(new RecordHeader(k, v.getBytes(StandardCharsets.UTF_8))));
        }
        return kafka.send(rec);
    }

    /** Convenience to send an EventEnvelope; uses subject as key and sets a few standard headers. */
    protected CompletableFuture<SendResult<String, Object>> sendEnvelope(String topic, EventEnvelope<?> env) {
        ProducerRecord<String, Object> rec = new ProducerRecord<>(topic, null, env.getSubject(), env);
        rec.headers().add(new RecordHeader("type", bytes(env.getType())));
        rec.headers().add(new RecordHeader("version", bytes(env.getVersion())));
        rec.headers().add(new RecordHeader("source", bytes(env.getSource())));
        if (env.getId() != null) {
            rec.headers().add(new RecordHeader(KafkaHeadersEx.CORRELATION_ID, bytes(env.getId())));
        }
        return kafka.send(rec);
    }

    /** Synchronous variant (use sparingly; prefer async). */
    protected SendResult<String, Object> sendAndAwait(String topic, String key, Object payload) throws Exception {
        return kafka.send(topic, key, payload).get();
    }

    private static byte[] bytes(String s) {
        return s == null ? new byte[0] : s.getBytes(StandardCharsets.UTF_8);
    }
}
