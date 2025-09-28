package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_media",
        indexes = {
                @Index(name = "idx_product_primary", columnList = "product_id, primary")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MediaType type; // IMAGE, VIDEO, THREE_D

    @Column(nullable = false)
    private Boolean primary = false;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
