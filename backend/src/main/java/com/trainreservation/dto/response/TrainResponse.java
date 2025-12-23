package com.trainreservation.dto.response;

import com.trainreservation.enums.CoachType;
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
public class TrainResponse {
    
    private Long id;
    private String trainNumber;
    private String trainName;
    private StationBasicResponse sourceStation;
    private StationBasicResponse destinationStation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal baseFare;
    private Boolean active;
    private String operatingDays;
    private List<CoachResponse> coaches;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoachResponse {
        private Long id;
        private String coachNumber;
        private CoachType coachType;
        private String coachTypeDescription;
        private Integer totalSeats;
        private Integer availableSeats;
        private BigDecimal fareMultiplier;
        private BigDecimal calculatedFare;
    }
}
