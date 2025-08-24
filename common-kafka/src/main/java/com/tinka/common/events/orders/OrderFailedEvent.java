package com.tinka.common.events.orders;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OrderFailedEvent extends BaseMarketplaceEvent {

    private String orderId;
    private String failureReason;
    private String buyerId;
    private boolean retryable;

    @Override
    public String getSource() {
        return "order-service";
    }
}
