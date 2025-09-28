package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_location",
        indexes = {
                @Index(name = "idx_country", columnList = "country"),
                @Index(name = "idx_city", columnList = "city")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(precision = 9, scale = 6)
    private Double latitude;

    @Column(precision = 9, scale = 6)
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
