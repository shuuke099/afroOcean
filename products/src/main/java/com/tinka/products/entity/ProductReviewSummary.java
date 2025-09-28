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

    private Double average = 0.0;
    private Integer count = 0;

    @Column(length = 2000)
    private String breakdownJson; // e.g., {"1":3, "2":1, "3":5, "4":20, "5":28}
}
