package com.trainreservation.service;

import com.trainreservation.dto.response.SeatResponse;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {

    List<SeatResponse> getSeatsForTrain(Long trainId, LocalDate journeyDate);
}
