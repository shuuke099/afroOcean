package com.tinka.common.events.products;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ProductVerifiedEvent extends BaseMarketplaceEvent {

    private String productId;
    private String verifiedBy;
    private boolean verified;
    private Instant verifiedAt;

    @Override
    public String getSource() {
        return "product-service";
    }
}
