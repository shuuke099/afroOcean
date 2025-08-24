package com.tinka.orders.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

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
    private String paymentMethod;
    private String shippingAddress;

    private String status;
    private LocalDateTime orderedAt;
    private LocalDateTime updatedAt;
}
