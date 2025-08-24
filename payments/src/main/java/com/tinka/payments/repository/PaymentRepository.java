package com.tinka.payments.repository;

import com.tinka.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);

    List<Payment> findByBuyerId(String buyerId);

    List<Payment> findBySellerId(String sellerId);

    List<Payment> findByStatus(String status); // optional for quick status queries

}
