package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double average;
    private Integer count;

    @Column(length = 2000)
    private String breakdownJson; // store breakdown as JSON string {1:10, 2:5,...}

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
