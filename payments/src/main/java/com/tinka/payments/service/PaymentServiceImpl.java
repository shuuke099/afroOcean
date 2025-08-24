package com.tinka.payments.service;

import com.tinka.payments.dto.PaymentRequest;
import com.tinka.payments.dto.PaymentResponse;
import com.tinka.payments.entity.Payment;
import com.tinka.payments.entity.PaymentStatus;
import com.tinka.payments.kafka.PaymentEventPublisher;
import com.tinka.payments.repository.PaymentRepository;
import com.tinka.common.events.payments.PaymentInitiatedEvent;
import com.tinka.common.events.payments.PaymentProcessedEvent;
import com.tinka.common.events.payments.PaymentFailedEvent;
import com.tinka.common.events.payments.RefundIssuedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher eventPublisher;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        Payment savedPending = null;

        try {
            // 1) Save as PENDING (initiated)
            Payment pending = Payment.builder()
                    .orderId(request.getOrderId())
                    .buyerId(request.getBuyerId())
                    .sellerId(request.getSellerId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .paymentMethod(request.getPaymentMethod())
                    .transactionReference(request.getTransactionReference())
                    .status(PaymentStatus.PENDING)
                    .build();

            savedPending = paymentRepository.save(pending);

            // Publish: PaymentInitiatedEvent
            eventPublisher.paymentInitiated(
                    PaymentInitiatedEvent.builder()
                            .paymentId(savedPending.getId().toString())
                            .orderId(savedPending.getOrderId())
                            .userId(savedPending.getBuyerId())
                            .amount(savedPending.getAmount())
                            .currency(savedPending.getCurrency())
                            .initiatedAt(toInstant(savedPending.getCreatedAt()))
                            .build()
            );

            // 2) Process (assume success for now)
            savedPending.setStatus(PaymentStatus.SUCCESS);
            savedPending.setPaidAt(LocalDateTime.now());
            Payment processed = paymentRepository.save(savedPending);

            // Publish: PaymentProcessedEvent
            eventPublisher.paymentProcessed(
                    PaymentProcessedEvent.builder()
                            .paymentId(processed.getId().toString())
                            .orderId(processed.getOrderId())
                            .amount(processed.getAmount())
                            .currency(processed.getCurrency())
                            .processedAt(toInstant(processed.getPaidAt()))
                            .transactionReference(processed.getTransactionReference())
                            .build()
            );

            return mapToResponse(processed);

        } catch (Exception ex) {
            if (savedPending != null) {
                eventPublisher.paymentFailed(
                        PaymentFailedEvent.builder()
                                .paymentId(savedPending.getId().toString())
                                .orderId(savedPending.getOrderId())
                                .amount(savedPending.getAmount())
                                .currency(savedPending.getCurrency())
                                .failureReason(ex.getMessage() != null ? ex.getMessage() : "UNKNOWN_ERROR")
                                .failedAt(toInstant(savedPending.getUpdatedAt()))
                                .build()
                );
                savedPending.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(savedPending);
            }
            throw ex;
        }
    }

    /**
     * Issue a refund for a payment and publish RefundIssuedEvent.
     * If refundAmount is null, refunds the full payment amount.
     */
    public PaymentResponse refund(Long paymentId, BigDecimal refundAmount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        // Guardrails
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            // idempotent-ish behavior: just return the current state
            return mapToResponse(payment);
        }
        if (payment.getStatus() == PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot refund a pending payment.");
        }

        BigDecimal amountToRefund = resolveRefundAmount(refundAmount, payment.getAmount());
        validateRefundAmount(amountToRefund, payment.getAmount());

        // Update entity
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setNotes(appendNote(payment.getNotes(),
                "REFUND " + amountToRefund + " " + (payment.getCurrency() == null ? "" : payment.getCurrency())
                        + (reason != null && !reason.isBlank() ? (" | Reason: " + reason) : "")));

        Payment updated = paymentRepository.save(payment);

        // Publish event
        eventPublisher.refundIssued(
                RefundIssuedEvent.builder()
                        .paymentId(updated.getId().toString())
                        .orderId(updated.getOrderId())
                        .amount(amountToRefund)
                        .currency(updated.getCurrency())
                        .refundedAt(toInstant(updated.getRefundedAt()))
                        .build()
        );

        return mapToResponse(updated);
    }

    @Override
    public List<PaymentResponse> getPaymentsByBuyer(String buyerId) {
        return paymentRepository.findByBuyerId(buyerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPaymentsBySeller(String sellerId) {
        return paymentRepository.findBySellerId(sellerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    // ---------- helpers ----------

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .buyerId(payment.getBuyerId())
                .sellerId(payment.getSellerId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .transactionReference(payment.getTransactionReference())
                .status(payment.getStatus().name())
                .paidAt(payment.getPaidAt())
                .build();
    }
    private static BigDecimal resolveRefundAmount(BigDecimal requested, BigDecimal original) {
        return requested != null ? requested : original;
    }

    private static void validateRefundAmount(BigDecimal amountToRefund, BigDecimal original) {
        if (amountToRefund == null) {
            throw new IllegalArgumentException("Refund amount is required.");
        }
        if (amountToRefund.signum() <= 0) {
            throw new IllegalArgumentException("Refund amount must be > 0.");
        }
        if (original != null && amountToRefund.compareTo(original) > 0) {
            throw new IllegalArgumentException("Refund amount exceeds original payment amount.");
        }
    }
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static java.time.Instant toInstant(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atZone(java.time.ZoneId.systemDefault()).toInstant();
    }

    private static String appendNote(String existing, String add) {
        if (isBlank(existing)) return add;
        return existing + " | " + add;
    }
}
