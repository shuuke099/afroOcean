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
public class ProductDeletedEvent extends BaseMarketplaceEvent {

    private String productId;
    private String sellerId;
    private String deletedBy;
    private Instant deletedAt;

    @Override
    public String getSource() {
        return "product-service";
    }
}
