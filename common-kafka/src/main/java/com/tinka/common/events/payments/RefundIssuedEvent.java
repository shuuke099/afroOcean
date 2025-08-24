package com.tinka.common.events.payments;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RefundIssuedEvent extends BaseMarketplaceEvent {

    private String refundId;
    private String paymentId;
    private String orderId;
    private String currency;
    private BigDecimal amount;
    private Instant refundedAt;

    @Override
    public String getSource() {
        return "payment-service";
    }
}
