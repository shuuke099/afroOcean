package com.tinka.products.dto;

import com.tinka.products.entity.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String title;
    private String description;

    private BigDecimal price;

    private String imageUrl;
    private List<String> images;

    private String category;
    private String brand;

    private String slug;
    private String country;
    private String currency;

    private Double averageRating;
    private Integer totalReviews;

    private Integer quantity;

    private boolean featured;
    private boolean verified;

    private ProductStatus status;

    private String sellerId;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
