package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String currency;
    private Double originalPrice;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
