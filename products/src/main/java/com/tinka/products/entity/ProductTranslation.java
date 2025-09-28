package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_translations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_language", columnNames = {"product_id", "language"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5, nullable = false)
    private String language; // ISO 639-1: en, so, ar, fr, es

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
