package com.tinka.orders.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Long productId;
    private String buyerId;
    private Integer quantity;
    private String paymentMethod;
    private String shippingAddress;
}
