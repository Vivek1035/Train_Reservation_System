package com.trainreservation.dto.response;

import com.trainreservation.enums.QuotaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponse {

    private Long id;
    private String seatNumber;
    private Boolean available;
    private QuotaType quotaType;

    private Boolean windowSeat;
    private Boolean aisleSeat;
    private Boolean lowerBerth;
    private Boolean upperBerth;

    private Long coachId; 
    private String coachCode;
    private String coachType;
}
