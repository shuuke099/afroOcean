package com.tinka.common.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public interface MarketplaceEvent extends Serializable {

    UUID getEventId();

    Instant getOccurredAt();

    String getEventType();

    int getEventVersion();

    String getSource();
}
