package com.tinka.payments.controller;

import com.tinka.payments.dto.PaymentRequest;
import com.tinka.payments.dto.PaymentResponse;
import com.tinka.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // --- Create payment (201 + Location) ---
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request,
                                                         UriComponentsBuilder uriBuilder) {
        PaymentResponse created = paymentService.processPayment(request);
        URI location = uriBuilder.path("/api/payments/order/{orderId}")
                .buildAndExpand(created.getOrderId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // --- Query by buyer ---
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByBuyer(@PathVariable String buyerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByBuyer(buyerId));
    }

    // --- Query by seller ---
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySeller(sellerId));
    }

    // --- Query by orderId (404 if not found) ---
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable String orderId) {
        PaymentResponse resp = paymentService.getPaymentByOrderId(orderId);
        return (resp == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(resp);
    }

    // --- Refund (full or partial) ---
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refund(@PathVariable Long paymentId,
                                                  @Valid @RequestBody RefundRequest request) {
        PaymentResponse resp = paymentService.refund(paymentId, request.getAmount(), request.getReason());
        return ResponseEntity.ok(resp);
    }

    // Simple local DTO for convenience. Move to its own file if you prefer.
    @Data
    public static class RefundRequest {
        // null -> full refund (handled by service)
        private BigDecimal amount;
        private String reason;
    }
}
