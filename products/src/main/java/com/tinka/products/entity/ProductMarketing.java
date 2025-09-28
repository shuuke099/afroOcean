package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_marketing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMarketing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Discount as percentage (0–100)
    @Column(nullable = false)
    private Double discount = 0.0;

    @Column(length = 255)
    private String promotion;

    @Column(nullable = false)
    private Boolean clearance = false;

    @Column(nullable = false)
    private Boolean featured = false;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
