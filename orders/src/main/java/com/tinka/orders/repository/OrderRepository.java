package com.tinka.orders.repository;

import com.tinka.orders.entity.Order;
import com.tinka.orders.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(String buyerId);

    List<Order> findBySellerId(String sellerId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByProductId(Long productId);

    boolean existsByBuyerIdAndProductId(String buyerId, Long productId);
}
