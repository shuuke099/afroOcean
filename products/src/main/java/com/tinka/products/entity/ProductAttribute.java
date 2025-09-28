package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_attributes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_key", columnNames = {"product_id", "keyName"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyName;

    @Column(nullable = false, length = 1000)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
