package com.tinka.common.events.orders;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderPlacedEvent extends BaseMarketplaceEvent {

    private String orderId;
    private String buyerId;
    private String sellerId;
    private List<String> productIds;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private Instant placedAt;

    @Override
    public String getSource() {
        return "orders"; // Update if you use a different service/module name
    }
}
