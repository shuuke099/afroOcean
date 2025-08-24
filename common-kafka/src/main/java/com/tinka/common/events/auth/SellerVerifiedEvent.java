package com.tinka.common.events.auth;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SellerVerifiedEvent extends BaseMarketplaceEvent {
    private String userId;
    private Long sellerId;
    private String businessName;
    private String taxId;
    private String region;
    private boolean verified;
    private Instant verifiedAt;

    @Override
    public String getSource() {
        return "auth"; // Update to actual service name if needed
    }
}
