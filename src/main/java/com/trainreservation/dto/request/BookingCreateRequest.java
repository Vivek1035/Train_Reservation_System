package com.trainreservation.dto.request;

import com.trainreservation.enums.QuotaType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCreateRequest {
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotNull(message = "Train ID is required")
    @Positive(message = "Train ID must be positive")
    private Long trainId;
    
    @NotNull(message = "Journey date is required")
    @Future(message = "Journey date must be in the future")
    private LocalDate journeyDate;
    
    @NotNull(message = "Passengers list is required")
    @Size(min = 1, max = 6, message = "Number of passengers must be between 1 and 6")
    @Valid
    private List<PassengerRequest> passengers;
    
    private String specialRequests;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PassengerRequest {
        
        @NotBlank(message = "Passenger name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        private String passengerName;
        
        @NotNull(message = "Passenger age is required")
        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age cannot exceed 120")
        private Integer passengerAge;
        
        @NotBlank(message = "Gender is required")
        @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE, or OTHER")
        private String passengerGender;
        
        @NotNull(message = "Quota type is required")
        private QuotaType quotaType;
        
        @NotNull(message = "Coach ID is required")
        @Positive(message = "Coach ID must be positive")
        private Long coachId;
        
        private String seatPreference; // WINDOW, AISLE, LOWER, UPPER, etc.
    }
}
