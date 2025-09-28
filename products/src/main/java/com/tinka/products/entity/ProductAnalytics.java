package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_analytics",
        indexes = {
                @Index(name = "idx_views", columnList = "views"),
                @Index(name = "idx_sales", columnList = "sales")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false)
    private Integer inquiries = 0;

    @Column(nullable = false)
    private Integer sales = 0;

    @Column(nullable = false)
    private Integer flags = 0;

    @Column(nullable = false)
    private Integer ratings = 0;

    @Column(nullable = false)
    private Integer wishlistCount = 0;

    @Column(nullable = false)
    private Integer cartAdds = 0;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
