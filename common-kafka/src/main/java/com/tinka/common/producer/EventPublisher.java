package com.tinka.common.producer;

import java.util.concurrent.CompletableFuture;

public interface EventPublisher<T> {
    CompletableFuture<?> publish(String topic, String key, T event);
}
