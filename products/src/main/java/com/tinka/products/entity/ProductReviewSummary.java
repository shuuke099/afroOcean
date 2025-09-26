package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewSummary {

    private Double average;
    private Integer count;

    @Column(length = 2000)
    private String breakdownJson; // e.g., {"1":3, "2":1, "3":5, "4":20, "5":28}
}
