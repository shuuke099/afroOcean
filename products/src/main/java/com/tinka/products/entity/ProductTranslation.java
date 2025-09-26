package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String language; // en, so, ar, fr, es
    private String category;
    private String name;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
