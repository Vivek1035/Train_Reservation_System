package com.trainreservation.dto.request;

import com.trainreservation.enums.CoachType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainCreateRequest {
    
    @NotBlank(message = "Train number is required")
    @Size(min = 4, max = 10, message = "Train number must be between 4 and 10 characters")
    private String trainNumber;
    
    @NotBlank(message = "Train name is required")
    @Size(min = 3, max = 100, message = "Train name must be between 3 and 100 characters")
    private String trainName;
    
    @NotNull(message = "Source station ID is required")
    @Positive(message = "Source station ID must be positive")
    private Long sourceStationId;
    
    @NotNull(message = "Destination station ID is required")
    @Positive(message = "Destination station ID must be positive")
    private Long destinationStationId;
    
    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;
    
    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;
    
    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be positive")
    @Max(value = 2000, message = "Total seats cannot exceed 2000")
    private Integer totalSeats;
    
    @NotNull(message = "Base fare is required")
    @DecimalMin(value = "0.01", message = "Base fare must be greater than 0")
    @DecimalMax(value = "10000.00", message = "Base fare cannot exceed 10000")
    private BigDecimal baseFare;
    
    @NotBlank(message = "Operating days are required")
    private String operatingDays; // e.g., "MON,TUE,WED,THU,FRI,SAT,SUN"
    
    @Builder.Default
    private Boolean active = true;
    
    private List<CoachCreateRequest> coaches;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoachCreateRequest {
        
        @NotBlank(message = "Coach number is required")
        private String coachNumber;
        
        @NotNull(message = "Coach type is required")
        private CoachType coachType;
        
        @NotNull(message = "Total seats is required")
        @Positive(message = "Total seats must be positive")
        private Integer totalSeats;
        
        @NotNull(message = "Fare multiplier is required")
        @DecimalMin(value = "0.1", message = "Fare multiplier must be at least 0.1")
        @DecimalMax(value = "5.0", message = "Fare multiplier cannot exceed 5.0")
        private BigDecimal fareMultiplier;
    }
}
