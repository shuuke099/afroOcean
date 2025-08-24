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
public class OrderConfirmedEvent extends BaseMarketplaceEvent {

    private String orderId;
    private String buyerId;
    private String sellerId;
    private Instant confirmedAt;

    @Override
    public String getSource() {
        return "orders";
    }
}
