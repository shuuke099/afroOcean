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

    private String weight;
    private String dimensions;
    private String deliveryEstimates;
    private String returnPolicy;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
