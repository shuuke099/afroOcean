package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer views;
    private Integer inquiries;
    private Integer sales;
    private Integer flags;
    private Integer ratings;
    private Integer wishlistCount;
    private Integer cartAdds;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
