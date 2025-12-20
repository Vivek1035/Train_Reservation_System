package com.trainreservation.controller;

import com.trainreservation.entity.Train;
import com.trainreservation.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    
    private final TrainService trainService;
    
    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
        Train createdTrain = trainService.createTrain(train);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrain);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        return trainService.getTrainById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains(@RequestParam(required = false) Boolean active) {
        List<Train> trains = active != null && active 
            ? trainService.getActiveTrains() 
            : trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
        return trainService.getTrainByNumber(trainNumber)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Train>> searchTrains(
            @RequestParam(required = false) Long sourceId,
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) String name) {
        
        if (sourceId != null && destinationId != null) {
            List<Train> trains = trainService.searchTrainsBetweenStations(sourceId, destinationId);
            return ResponseEntity.ok(trains);
        }
        
        if (name != null) {
            List<Train> trains = trainService.searchTrainsByName(name);
            return ResponseEntity.ok(trains);
        }
        
        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Train>> getTrainsWithAvailableSeats() {
        List<Train> trains = trainService.getTrainsWithAvailableSeats();
        return ResponseEntity.ok(trains);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long id, @RequestBody Train train) {
        Train updatedTrain = trainService.updateTrain(id, train);
        return ResponseEntity.ok(updatedTrain);
    }
    
    @PatchMapping("/{id}/seats")
    public ResponseEntity<Train> updateAvailableSeats(
            @PathVariable Long id, 
            @RequestParam Integer change) {
        Train updatedTrain = trainService.updateAvailableSeats(id, change);
        return ResponseEntity.ok(updatedTrain);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}
