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
public class PaymentInitiatedEvent extends BaseMarketplaceEvent {

    private String paymentId;
    private String orderId;
    private String currency;
    private String userId;
    private BigDecimal amount;
    private String paymentMethod;
    private Instant initiatedAt;

    @Override
    public String getSource() {
        return "payment-service";
    }
}
