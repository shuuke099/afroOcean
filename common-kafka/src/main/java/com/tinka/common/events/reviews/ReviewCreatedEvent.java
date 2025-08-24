package com.tinka.common.events.reviews;

import com.tinka.common.events.BaseMarketplaceEvent;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ReviewCreatedEvent extends BaseMarketplaceEvent {

    private String reviewId;
    private String productId;
    private String userId;
    private String orderId;
    private String sellerId;
    private Double rating;
    private String comment;
    private Instant createdAt;
    private String source;

    @Override
    public String getSource() {
        return "review-service"; // Adjust based on your actual service/module name
    }
}
