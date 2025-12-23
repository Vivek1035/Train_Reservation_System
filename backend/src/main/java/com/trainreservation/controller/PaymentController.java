package com.trainreservation.controller;

import com.trainreservation.dto.ApiResponse;
import com.trainreservation.dto.request.PaymentRequest;
import com.trainreservation.dto.response.PaymentResponse;
import com.trainreservation.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Payment Controller
 * Handles payment processing endpoints
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Process payment for a booking
     * POST /api/payments/process
     */
    @PostMapping("/process")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.processPayment(request);

        if (response.getStatus().name().equals("SUCCESS")) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Payment processed successfully", response));
        } else {
            return ResponseEntity
                    .status(HttpStatus.PAYMENT_REQUIRED)
                    .body(ApiResponse.success("Payment failed. Please try again.", response));
        }
    }

    /**
     * Retry failed payment
     * POST /api/payments/{paymentId}/retry
     */
    @PostMapping("/{paymentId}/retry")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> retryPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.retryPayment(paymentId, request);

        if (response.getStatus().name().equals("SUCCESS")) {
            return ResponseEntity.ok(
                    ApiResponse.success("Payment retry successful", response)
            );
        } else {
            return ResponseEntity
                    .status(HttpStatus.PAYMENT_REQUIRED)
                    .body(ApiResponse.success("Payment retry failed", response));
        }
    }

    /**
     * Get payment by transaction ID
     * GET /api/payments/transaction/{transactionId}
     */
    @GetMapping("/transaction/{transactionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByTransactionId(
            @PathVariable String transactionId) {

        PaymentResponse response = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get payment by booking ID
     * GET /api/payments/booking/{bookingId}
     */
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByBookingId(
            @PathVariable Long bookingId) {

        PaymentResponse response = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Get all payments by user
     * GET /api/payments/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByUserId(
            @PathVariable Long userId) {

        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Found " + payments.size() + " payment(s)", payments)
        );
    }

    /**
     * Refund payment (Admin or for cancelled bookings)
     * POST /api/payments/{paymentId}/refund
     */
    @PostMapping("/{paymentId}/refund")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(
            @PathVariable Long paymentId) {

        PaymentResponse response = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(
                ApiResponse.success("Refund processed successfully", response)
        );
    }

    /**
     * Health check endpoint
     * GET /api/payments/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(
                ApiResponse.success("Payment service is operational", "OK")
        );
    }
}
