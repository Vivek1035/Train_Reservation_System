package com.trainreservation.controller;

import com.trainreservation.dto.response.SeatResponse;
import com.trainreservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{trainId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeatsForTrain(
            @PathVariable("trainId") Long trainId,
            @RequestParam(name = "journeyDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate journeyDate
    ) {
        if (journeyDate == null) {
            journeyDate = LocalDate.now();
        }

        return ResponseEntity.ok(
                seatService.getSeatsForTrain(trainId, journeyDate)
        );
    }
}
