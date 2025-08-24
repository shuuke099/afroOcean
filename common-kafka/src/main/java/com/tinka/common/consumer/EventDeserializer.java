package com.tinka.common.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Getter
public class EventDeserializer {

    private final ObjectMapper mapper;

    public EventDeserializer() {
        this(defaultMapper());
    }

    public EventDeserializer(ObjectMapper mapper) {
        this.mapper = mapper != null ? mapper : defaultMapper();
    }

    public <T> T fromJson(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Failed to deserialize JSON to {}: {}", type.getSimpleName(), shorten(json), e);
            throw new RuntimeException("Failed to deserialize " + type.getSimpleName(), e);
        }
    }

    public <T> Optional<T> tryFromJson(String json, Class<T> type) {
        try {
            if (json == null) return Optional.empty();
            return Optional.of(mapper.readValue(json, type));
        } catch (Exception e) {
            log.warn("Non-fatal: could not deserialize JSON to {}: {}", type.getSimpleName(), shorten(json));
            return Optional.empty();
        }
    }

    public <T> T fromBytes(byte[] bytes, Class<T> type) {
        if (bytes == null) return null;
        try {
            return mapper.readValue(bytes, type);
        } catch (Exception e) {
            log.error("Failed to deserialize bytes to {} (len={}): {}", type.getSimpleName(), bytes.length, e.getMessage(), e);
            throw new RuntimeException("Failed to deserialize " + type.getSimpleName(), e);
        }
    }

    public String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize {}: {}", value == null ? "null" : value.getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException("Failed to serialize object", e);
        }
    }

    // ---- helpers ----

    private static ObjectMapper defaultMapper() {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return m;
    }

    private static String shorten(String s) {
        if (s == null) return "null";
        s = s.replaceAll("\\s+", " ").trim();
        return s.length() > 200 ? s.substring(0, 200) + "..." : s;
    }
}
