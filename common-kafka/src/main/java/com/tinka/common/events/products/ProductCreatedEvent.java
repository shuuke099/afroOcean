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
public class ProductCreatedEvent extends BaseMarketplaceEvent {

    private String productId;
    private String sellerId;
    private String name;
    private String category;
    private BigDecimal price;
    private String status;
    private Instant createdAt;

    @Override
    public String getSource() {
        return "product-service";
    }
}
