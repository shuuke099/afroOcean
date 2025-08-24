package com.tinka.common.events.orders;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderCancelledEvent extends BaseMarketplaceEvent {

    private String orderId;
    private String buyerId;
    private String sellerId;
    private String cancelledBy; // userId or adminId
    private String reason;
    private Instant cancelledAt;

    @Override
    public String getSource() {
        return "orders";
    }
}
