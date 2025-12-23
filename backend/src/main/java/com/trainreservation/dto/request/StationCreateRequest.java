package com.trainreservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationCreateRequest {
    
    @NotBlank(message = "Station code is required")
    @Size(min = 2, max = 10, message = "Station code must be between 2 and 10 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Station code must contain only uppercase letters and numbers")
    private String stationCode;
    
    @NotBlank(message = "Station name is required")
    @Size(min = 3, max = 100, message = "Station name must be between 3 and 100 characters")
    private String stationName;
    
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;
    
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    private String pincode;
}
