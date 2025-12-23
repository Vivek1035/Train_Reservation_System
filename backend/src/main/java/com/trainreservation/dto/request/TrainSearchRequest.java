package com.trainreservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainSearchRequest {
    
    @NotNull(message = "Source station ID is required")
    @Positive(message = "Source station ID must be positive")
    private Long sourceStationId;
    
    @NotNull(message = "Destination station ID is required")
    @Positive(message = "Destination station ID must be positive")
    private Long destinationStationId;
    
    private LocalDate journeyDate;
    
    @Builder.Default
    private Boolean onlyAvailable = true;
}
