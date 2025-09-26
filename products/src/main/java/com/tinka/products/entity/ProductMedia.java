package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String type; // image, video, 3d
    private Boolean primary;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
