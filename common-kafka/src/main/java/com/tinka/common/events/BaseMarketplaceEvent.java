package com.tinka.common.events;

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public abstract class BaseMarketplaceEvent implements MarketplaceEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID eventId = UUID.randomUUID();
    private final Instant occurredAt = Instant.now();

    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getEventVersion() {
        return 1;
    }

    @Override
    public abstract String getSource();
}
