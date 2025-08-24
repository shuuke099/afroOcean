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
public class PaymentFailedEvent extends BaseMarketplaceEvent {

    private String paymentId;
    private String orderId;
    private String failureReason;
    private String currency;
    private BigDecimal amount;
    private Instant failedAt;
    private boolean retryable;

    @Override
    public String getSource() {
        return "payment-service";
    }
}
