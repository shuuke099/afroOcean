package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_seo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSEO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String seoTitle;

    @Column(length = 160)
    private String seoDescription;

    @Column(length = 2000)
    private String searchKeywords; // comma-separated or JSON

    @Column(length = 1000)
    private String tags; // comma-separated

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
