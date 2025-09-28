package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "product_prices",
        indexes = {
                @Index(name = "idx_amount", columnList = "amount")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency; // ISO 4217 (e.g., USD, EUR)

    @Column(precision = 19, scale = 4)
    private BigDecimal originalPrice;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
