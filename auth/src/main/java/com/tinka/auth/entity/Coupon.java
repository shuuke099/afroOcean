package com.tinka.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String description;

    private double discountAmount;

    private LocalDateTime expiryDate;

    private boolean used;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
