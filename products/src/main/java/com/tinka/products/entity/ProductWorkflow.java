package com.tinka.products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_workflow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; // draft, pending, approved, rejected
    private String moderatedBy;
    private String reason;
    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
