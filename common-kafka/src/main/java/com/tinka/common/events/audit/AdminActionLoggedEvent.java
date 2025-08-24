package com.tinka.common.events.audit;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AdminActionLoggedEvent extends BaseMarketplaceEvent {

    private String adminId;
    private String actionType;       // e.g., "BLOCK_USER", "DELETE_PRODUCT", "GRANT_REFUND"
    private String targetId;         // could be userId, productId, orderId, etc.
    private String targetType;       // e.g., "USER", "PRODUCT", "ORDER"
    private String description;      // human-readable explanation of the action
    private Instant actionTime;

    @Override
    public String getSource() {
        return "admin-panel";
    }
}
