package com.tinka.products.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Core product information
    private String title;

    @Column(length = 1000)
    private String description;

    private BigDecimal price;

    private String imageUrl; // Primary image

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images; // Additional images

    private String category;
    private String brand;

    // SEO and filtering
    @Column(unique = true)
    private String slug;

    private String country;
    private String currency;

    // Ratings (cached from review microservice)
    private Double averageRating;
    private Integer totalReviews;

    private Integer quantity;

    private boolean featured;
    private boolean verified;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String sellerId; // UUID or ID from seller microservice

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == ProductStatus.PUBLISHED) {
            this.publishedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.status == ProductStatus.PUBLISHED && this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
    }
}
