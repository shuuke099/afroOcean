package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;
    private String sku;
    private String stockStatus; // in_stock, low, out_of_stock

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
