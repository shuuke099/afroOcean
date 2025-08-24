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
public class PaymentProcessedEvent extends BaseMarketplaceEvent {

    private String paymentId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private Instant processedAt;
    private String transactionReference;

    @Override
    public String getSource() {
        return "payment-service";
    }
}
