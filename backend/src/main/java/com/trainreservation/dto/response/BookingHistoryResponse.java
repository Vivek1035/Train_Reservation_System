package com.trainreservation.dto.response;

import com.trainreservation.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHistoryResponse {
    
    private Long id;
    private String pnrNumber;
    private BookingStatus status;
    private LocalDate journeyDate;
    private Integer numberOfPassengers;
    private BigDecimal totalFare;
    private LocalDateTime bookedAt;
    
    // Simplified train info for history
    private String trainNumber;
    private String trainName;
    private String route; // "Delhi -> Mumbai"
    
    private Boolean canCancel;
    private Boolean isPastJourney;
}
