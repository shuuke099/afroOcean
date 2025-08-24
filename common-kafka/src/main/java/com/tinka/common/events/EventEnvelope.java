package com.tinka.common.events;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.Map;

/**
 * Generic envelope for events on the wire.
 * Keep payload type T small and serializable (POJO).
 */
@Value
@Builder
public class EventEnvelope<T> {
    String id;                 // correlation id / event id (UUID)
    String type;               // e.g., "order.placed"
    String version;            // e.g., "v1"
    String source;             // e.g., "orders-service"
    String subject;            // e.g., aggregate id for partition key
    Instant time;              // event time
    Map<String, String> attributes; // optional metadata
    T data;                    // actual payload
}
