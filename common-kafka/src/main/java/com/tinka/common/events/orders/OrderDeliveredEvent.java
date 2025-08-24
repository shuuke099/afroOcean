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
public class OrderDeliveredEvent extends BaseMarketplaceEvent {

    private String orderId;
    private String deliveryAgent;
    private String buyerId;
    private String sellerId;
    private Instant deliveredAt;

    @Override
    public String getSource() {
        return "orders";
    }
}
