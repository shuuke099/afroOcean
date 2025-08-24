package com.tinka.common.events.products;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ProductOutOfStockEvent extends BaseMarketplaceEvent {

    private String productId;
    private String sellerId;
    private int availableQuantity;

    @Override
    public String getSource() {
        return "product-service";
    }
}
