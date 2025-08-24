package com.tinka.common.events.products;

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
public class ProductUpdatedEvent extends BaseMarketplaceEvent {

    private String productId;
    private String sellerId;
    private String updatedBy;
    private String name;
    private String status;
    private String category;
    private BigDecimal price;
    private boolean available;
    private Instant updatedAt;

    @Override
    public String getSource() {
        return "product-service"; // Replace with your actual service name if different
    }
}
