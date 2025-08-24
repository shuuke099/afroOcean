package com.tinka.orders.service;

import com.tinka.orders.dto.OrderRequest;
import com.tinka.orders.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getOrdersByBuyerId(String buyerId);

    List<OrderResponse> getOrdersBySellerId(String sellerId);

    void cancelOrder(Long orderId);

    void markAsShipped(Long orderId);

    void markAsDelivered(Long orderId);
}
