package com.tinka.products.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String slug;
    private String sellerId;
    private String status;

    private Map<String, TranslationDto> translations;
    private PriceDto price;
    private InventoryDto inventory;
    private List<MediaDto> media;
    private MarketingDto marketing;
    private SeoDto seo;
    private LocationDto location;
    private List<AttributeDto> attributes;
    private ShippingDto shipping;

    private AnalyticsDto analytics;
    private ReviewSummaryDto reviews;
    private WorkflowDto workflow;
    private AuditDto audit;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class AnalyticsDto {
    private Integer views;
    private Integer inquiries;
    private Integer sales;
    private Integer flags;
    private Integer ratings;
    private Integer wishlistCount;
    private Integer cartAdds;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class ReviewSummaryDto {
    private Double average;
    private Integer count;
    private Map<Integer,Integer> breakdown;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class WorkflowDto {
    private String status;
    private String moderatedBy;
    private String reason;
    private String timestamp;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class AuditDto {
    private String createdAt;
    private String updatedAt;
    private String publishedAt;
    private String deletedAt;
}
