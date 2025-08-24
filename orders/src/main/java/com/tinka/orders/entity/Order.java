package com.tinka.orders.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String productTitle;
    private String productImage;

    private String sellerId;
    private String buyerId;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private String currency;
    private String country;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String paymentMethod;
    private String shippingAddress;

    private LocalDateTime orderedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.orderedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
