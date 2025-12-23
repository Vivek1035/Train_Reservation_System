package com.trainreservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationBasicResponse {
    
    private Long id;
    private String stationCode;
    private String stationName;
    private String city;
    private String state;
}
