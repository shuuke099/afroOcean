package com.tinka.common.config;

/**
 * Standard header keys to use across producers/consumers.
 * (Keep names lowercase; headers are bytes on the wire.)
 */
public final class KafkaHeadersEx {
    public static final String CORRELATION_ID = "x-correlation-id";
    public static final String CAUSATION_ID   = "x-causation-id";
    public static final String TENANT_ID      = "x-tenant-id";
    public static final String EVENT_TYPE     = "x-event-type";
    public static final String EVENT_VERSION  = "x-event-version";
    public static final String EVENT_SOURCE   = "x-event-source";

    private KafkaHeadersEx() {}
}

