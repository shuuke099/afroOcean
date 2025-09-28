package com.tinka.products.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private Map<String, TranslationDto> translations;  // { "en": { name, category, description }, ... }
    private PriceDto price;
    private InventoryDto inventory;
    private List<MediaDto> media;
    private MarketingDto marketing;
    private SeoDto seo;
    private LocationDto location;
    private List<AttributeDto> attributes;
    private ShippingDto shipping;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class TranslationDto {
    private String category;
    private String name;
    private String description;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class PriceDto {
    private Double amount;
    private String currency;
    private Double originalPrice;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class InventoryDto {
    private Integer quantity;
    private String sku;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class MediaDto {
    private String url;
    private String type;
    private Boolean primary;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class MarketingDto {
    private Double discount;
    private String promotion;
    private Boolean clearance;
    private Boolean featured;
    private String startDate;
    private String endDate;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class SeoDto {
    private String seoTitle;
    private String seoDescription;
    private List<String> searchKeywords;
    private List<String> tags;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class LocationDto {
    private String country;
    private String city;
    private Double lat;
    private Double lon;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class AttributeDto {
    private String key;
    private String value;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class ShippingDto {
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String deliveryEstimates;
    private String returnPolicy;
}
