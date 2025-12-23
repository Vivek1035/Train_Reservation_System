package com.trainreservation.service.impl;

import com.trainreservation.dto.response.SeatResponse;
import com.trainreservation.entity.Seat;
import com.trainreservation.repository.SeatRepository;
import com.trainreservation.repository.TrainRepository;
import com.trainreservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;

    @Override
    public List<SeatResponse> getSeatsForTrain(Long trainId, LocalDate journeyDate) {

        // Validate train exists
        trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        // 🔥 Use your existing JPQL query
        List<Seat> seats = seatRepository.findAvailableSeatsForTrain(trainId);

        return seats.stream()
                .map(seat -> SeatResponse.builder()
                        .id(seat.getId())
                        .seatNumber(seat.getSeatNumber())
                        .available(seat.getAvailable())
                        .quotaType(seat.getQuotaType())
                        .windowSeat(Boolean.TRUE.equals(seat.getIsWindowSeat()))
                        .aisleSeat(Boolean.TRUE.equals(seat.getIsAisleSeat()))
                        .lowerBerth(Boolean.TRUE.equals(seat.getIsLowerBerth()))
                        .upperBerth(Boolean.TRUE.equals(seat.getIsUpperBerth()))
                        .coachCode(seat.getCoach().getCoachNumber())
                        .coachType(seat.getCoach().getCoachType().name())
                        .coachId(seat.getCoach().getId())
                        .build())
                .toList();
    }
}
