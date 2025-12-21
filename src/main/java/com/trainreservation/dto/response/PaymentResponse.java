package com.trainreservation.dto.response;

import com.trainreservation.enums.PaymentMethod;
import com.trainreservation.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for payment response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private String pnrNumber;
    private String transactionId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String statusMessage;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;

    // For frontend display
    private String maskedCardNumber; // e.g., "****1234"
    private Boolean canRetry;
}
