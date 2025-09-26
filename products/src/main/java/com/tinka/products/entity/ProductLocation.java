package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String coordinates; // store as "lat,long"

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
