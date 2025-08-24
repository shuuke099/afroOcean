package com.tinka.orders.service;

import com.tinka.orders.dto.OrderRequest;
import com.tinka.orders.dto.OrderResponse;
import com.tinka.orders.entity.Order;
import com.tinka.orders.entity.OrderStatus;
import com.tinka.orders.repository.OrderRepository;
import com.tinka.orders.kafka.OrderEventPublisher; // <--- NEW: Import your publisher
import com.tinka.common.events.orders.*; // <--- Import order event classes from common-kafka
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher; // <--- Inject publisher

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        BigDecimal unitPrice = BigDecimal.valueOf(10.00); // Placeholder, replace with real lookup
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .productId(request.getProductId())
                .buyerId(request.getBuyerId())
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .paymentMethod(request.getPaymentMethod())
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .build();

        Order saved = orderRepository.save(order);

        // --- Publish OrderPlacedEvent (match your event class fields) ---
        eventPublisher.orderPlaced(OrderPlacedEvent.builder()
                        .orderId(String.valueOf(saved.getId()))
                        .buyerId(saved.getBuyerId()) // userId is buyer
                        .productIds(List.of(String.valueOf(saved.getProductId()))) // wrap single productId in a list for compatibility
                        .totalAmount(saved.getTotalPrice())
                        .paymentStatus(saved.getStatus().name())
                        .placedAt(saved.getOrderedAt() != null
                                ? saved.getOrderedAt().atZone(ZoneId.systemDefault()).toInstant()
                                : Instant.now())
                        .build()
        );

        return mapToResponse(saved);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderResponse> getOrdersByBuyerId(String buyerId) {
        return orderRepository.findByBuyerId(buyerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrdersBySellerId(String sellerId) {
        return orderRepository.findBySellerId(sellerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        Order updated = orderRepository.save(order);

        // --- Publish OrderStatusChangedEvent (Cancelled) ---
        eventPublisher.orderCancelled(
                OrderCancelledEvent.builder()
                        .orderId(String.valueOf(updated.getId()))
                        .buyerId(updated.getBuyerId())
                        .sellerId(updated.getSellerId())
                        .cancelledAt(updated.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
                        .build()
        );
    }

    @Override
    public void markAsShipped(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.SHIPPED);
        Order updated = orderRepository.save(order);

        // --- Publish OrderStatusChangedEvent (Shipped) ---
        eventPublisher.orderShipped(
                OrderShippedEvent.builder()
                        .orderId(String.valueOf(updated.getId()))
                        .buyerId(updated.getBuyerId())
                        .sellerId(updated.getSellerId())
                        .shippedAt(updated.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())

                        .build()
        );
    }

    @Override
    public void markAsDelivered(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.DELIVERED);
        Order updated = orderRepository.save(order);

        // --- Publish OrderStatusChangedEvent (Delivered) ---
        eventPublisher.orderDelivered(
                OrderDeliveredEvent.builder()
                        .orderId(String.valueOf(updated.getId()))
                        .buyerId(updated.getBuyerId())
                        .sellerId(updated.getSellerId())
                        .deliveredAt(updated.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
                        .build()
        );
    }

    // Utility method to map entity to DTO
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .productTitle(order.getProductTitle())
                .productImage(order.getProductImage())
                .sellerId(order.getSellerId())
                .buyerId(order.getBuyerId())
                .quantity(order.getQuantity())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .currency(order.getCurrency())
                .country(order.getCountry())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .status(order.getStatus().name())
                .orderedAt(order.getOrderedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
