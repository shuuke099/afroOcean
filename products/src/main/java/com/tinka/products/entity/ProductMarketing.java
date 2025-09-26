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

    private Double discount;
    private String promotion;
    private Boolean clearance;
    private Boolean featured;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
