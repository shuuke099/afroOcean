package com.tinka.orders.controller;

import com.tinka.orders.dto.OrderRequest;
import com.tinka.orders.dto.OrderResponse;
import com.tinka.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByBuyer(@PathVariable String buyerId) {
        return ResponseEntity.ok(orderService.getOrdersByBuyerId(buyerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(orderService.getOrdersBySellerId(sellerId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Void> markAsShipped(@PathVariable Long orderId) {
        orderService.markAsShipped(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Void> markAsDelivered(@PathVariable Long orderId) {
        orderService.markAsDelivered(orderId);
        return ResponseEntity.noContent().build();
    }
}
