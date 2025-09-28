package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_shipping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductShipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Weight in kilograms
    @Column(precision = 10, scale = 2)
    private Double weight;

    // Dimensions in centimeters
    @Column(precision = 10, scale = 2)
    private Double length;

    @Column(precision = 10, scale = 2)
    private Double width;

    @Column(precision = 10, scale = 2)
    private Double height;

    // Delivery estimates in days
    private Integer minDeliveryDays;
    private Integer maxDeliveryDays;

    // Text description of return policy
    @Column(length = 1000)
    private String returnPolicy;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
