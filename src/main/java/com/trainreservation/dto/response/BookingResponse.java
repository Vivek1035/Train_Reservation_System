package com.trainreservation.dto.response;

import com.trainreservation.enums.BookingStatus;
import com.trainreservation.enums.QuotaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    
    private Long id;
    private String pnrNumber;
    private BookingStatus status;
    private LocalDate journeyDate;
    private Integer numberOfPassengers;
    private BigDecimal totalFare;
    private String remarks;
    private LocalDateTime bookedAt;
    
    // Train details
    private TrainBasicInfo train;
    
    // Passengers with seat details
    private List<ReservationInfo> reservations;
    
    // Payment details
    private PaymentInfo payment;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TrainBasicInfo {
        private Long id;
        private String trainNumber;
        private String trainName;
        private String sourceStation;
        private String destinationStation;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservationInfo {
        private Long id;
        private String passengerName;
        private Integer passengerAge;
        private String passengerGender;
        private QuotaType quotaType;
        private String coachNumber;
        private String seatNumber;
        private BigDecimal fare;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentInfo {
        private Long id;
        private String transactionId;
        private BigDecimal amount;
        private String paymentMethod;
        private String status;
        private LocalDateTime paymentDate;
    }
}
