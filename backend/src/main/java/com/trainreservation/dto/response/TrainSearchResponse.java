package com.trainreservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainSearchResponse {
    
    private Long id;
    private String trainNumber;
    private String trainName;
    private String sourceStationCode;
    private String sourceStationName;
    private String destinationStationCode;
    private String destinationStationName;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String duration;
    private Integer availableSeats;
    private BigDecimal startingFare;
    private Boolean hasAvailability;
}
