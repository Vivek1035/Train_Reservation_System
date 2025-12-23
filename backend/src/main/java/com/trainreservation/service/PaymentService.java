package com.trainreservation.service;

import com.trainreservation.dto.request.PaymentRequest;
import com.trainreservation.dto.response.PaymentResponse;
import com.trainreservation.entity.Booking;
import com.trainreservation.entity.Payment;
import com.trainreservation.enums.BookingStatus;
import com.trainreservation.enums.PaymentStatus;
import com.trainreservation.exception.InvalidOperationException;
import com.trainreservation.exception.ResourceNotFoundException;
import com.trainreservation.repository.BookingRepository;
import com.trainreservation.repository.PaymentRepository;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Payment Service - Simulates payment processing
 * This is a DUMMY implementation for development/testing
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final Random random = new Random();

    /**
     * Process payment for a booking
     * Simulates payment gateway interaction
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("Processing payment for booking ID: {}", request.getBookingId());

        // 1. Validate booking exists
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", request.getBookingId()));

        // 2. Validate booking status
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidOperationException("Cannot process payment for cancelled booking");
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new InvalidOperationException("Payment already completed for this booking");
        }

        // 3. Validate amount matches booking total fare
        if (request.getAmount().compareTo(booking.getTotalFare()) != 0) {
            throw new InvalidOperationException(
                    String.format("Payment amount ₹%.2f does not match booking total ₹%.2f",
                            request.getAmount(), booking.getTotalFare())
            );
        }

        // 4. Check if payment already exists for this booking
        paymentRepository.findByBooking(booking).ifPresent(existingPayment -> {
            if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                throw new InvalidOperationException("Payment already completed for this booking");
            }
        });

        // 5. Simulate payment processing
        PaymentSimulationResult simulationResult = simulatePaymentGateway(request);

        // 6. Create payment record
        Payment payment = Payment.builder()
                .booking(booking)
                .transactionId(generateTransactionId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(simulationResult.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .paymentGatewayResponse(simulationResult.getGatewayResponse())
                .paymentDate(simulationResult.isSuccess() ? LocalDateTime.now() : null)
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment saved with transaction ID: {}", payment.getTransactionId());

        // 7. Update booking status based on payment result
        if (simulationResult.isSuccess()) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            log.info("Booking {} confirmed after successful payment", booking.getPnrNumber());
        } else {
            // Keep booking as PENDING, allow retry
            log.warn("Payment failed for booking {}: {}", booking.getPnrNumber(), simulationResult.getGatewayResponse());
        }

        // 8. Return response
        return mapToPaymentResponse(payment, booking, simulationResult.getStatusMessage());
    }

    /**
     * Retry failed payment
     */
    public PaymentResponse retryPayment(Long paymentId, PaymentRequest request) {
        log.info("Retrying payment ID: {}", paymentId);

        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
            throw new InvalidOperationException("Cannot retry successful payment");
        }

        // Process new payment attempt
        return processPayment(request);
    }

    /**
     * Get payment by transaction ID
     */
    public PaymentResponse getPaymentByTransactionId(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "transactionId", transactionId));

        return mapToPaymentResponse(payment, payment.getBooking(), getStatusMessage(payment.getStatus()));
    }

    /**
     * Get payment by booking ID
     */
    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        Payment payment = paymentRepository.findByBooking(booking)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking", "bookingId", bookingId));

        return mapToPaymentResponse(payment, booking, getStatusMessage(payment.getStatus()));
    }

    /**
     * Get all payments by user
     */
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(payment -> mapToPaymentResponse(payment, payment.getBooking(), getStatusMessage(payment.getStatus())))
                .collect(Collectors.toList());
    }

    /**
     * Refund payment (for cancelled bookings)
     */
    public PaymentResponse refundPayment(Long paymentId) {
        log.info("Processing refund for payment ID: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidOperationException("Can only refund successful payments");
        }

        Booking booking = payment.getBooking();
        if (booking.getStatus() != BookingStatus.CANCELLED) {
            throw new InvalidOperationException("Booking must be cancelled before refund");
        }

        // Simulate refund processing
        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setPaymentGatewayResponse("Refund processed successfully on " + LocalDateTime.now());
        payment = paymentRepository.save(payment);

        log.info("Refund completed for transaction ID: {}", payment.getTransactionId());

        return mapToPaymentResponse(payment, booking, "Refund processed successfully");
    }

    /**
     * SIMULATION: Simulate payment gateway interaction
     * In production, this would call actual payment gateway APIs
     */
    private PaymentSimulationResult simulatePaymentGateway(PaymentRequest request) {
        log.info("Simulating payment gateway for amount: ₹{}", request.getAmount());

        // Simulate processing delay
        try {
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Force failure if requested (for testing)
        if (Boolean.TRUE.equals(request.getForceFailure())) {
            return PaymentSimulationResult.failure("Payment declined by test configuration");
        }

        // Simulate card validation
        String cardNumber = request.getCardNumber().replaceAll("\\s+", "");

        // Test card numbers for specific outcomes
        if (cardNumber.endsWith("0000")) {
            return PaymentSimulationResult.failure("Insufficient funds");
        } else if (cardNumber.endsWith("1111")) {
            return PaymentSimulationResult.failure("Card expired");
        } else if (cardNumber.endsWith("2222")) {
            return PaymentSimulationResult.failure("Invalid CVV");
        } else if (cardNumber.endsWith("3333")) {
            return PaymentSimulationResult.failure("Card blocked by issuer");
        }

        // Random failure (10% chance)
        if (random.nextInt(100) < 10) {
            String[] failureReasons = {
                    "Transaction timeout",
                    "Card declined",
                    "Network error",
                    "Daily limit exceeded"
            };
            return PaymentSimulationResult.failure(failureReasons[random.nextInt(failureReasons.length)]);
        }

        // Success
        return PaymentSimulationResult.success("Payment processed successfully");
    }

    /**
     * Generate unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Mask card number for display
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }

    /**
     * Get status message
     */
    private String getStatusMessage(PaymentStatus status) {
        return switch (status) {
            case SUCCESS -> "Payment completed successfully";
            case FAILED -> "Payment failed. Please try again.";
            case PENDING -> "Payment is being processed";
            case REFUNDED -> "Payment has been refunded";
        };
    }

    /**
     * Map Payment entity to PaymentResponse DTO
     */
    private PaymentResponse mapToPaymentResponse(Payment payment, Booking booking, String statusMessage) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(booking.getId())
                .pnrNumber(booking.getPnrNumber())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .statusMessage(statusMessage)
                .paymentDate(payment.getPaymentDate())
                .createdAt(payment.getCreatedAt())
                .maskedCardNumber("****") // Card details not stored
                .canRetry(payment.getStatus() == PaymentStatus.FAILED)
                .build();
    }

    /**
     * Inner class to hold simulation result
     */
    @Data
    @AllArgsConstructor
    private static class PaymentSimulationResult {
        private boolean success;
        private String statusMessage;
        private String gatewayResponse;

        static PaymentSimulationResult success(String message) {
            return new PaymentSimulationResult(true, message,
                    "Gateway Response: SUCCESS | Auth Code: " + UUID.randomUUID().toString().substring(0, 8));
        }

        static PaymentSimulationResult failure(String reason) {
            return new PaymentSimulationResult(false, "Payment failed: " + reason,
                    "Gateway Response: FAILED | Reason: " + reason);
        }
    }
}
