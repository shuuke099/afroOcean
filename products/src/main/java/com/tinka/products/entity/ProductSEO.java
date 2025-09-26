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

    private String seoTitle;
    private String seoDescription;

    @Column(length = 2000)
    private String searchKeywords; // comma-separated

    private String tags; // optional CSV tags

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
