package com.trainreservation.controller;

import com.trainreservation.dto.response.StationResponse;
import com.trainreservation.entity.Station;
import com.trainreservation.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {
    
    private final StationService stationService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        Station createdStation = stationService.createStation(station);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStation);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        return stationService.getStationById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<StationResponse>> getAllStations() {
        List<StationResponse> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    
    @GetMapping("/code/{code}")
    public ResponseEntity<Station> getStationByCode(@PathVariable String code) {
        return stationService.getStationByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Station>> searchStations(@RequestParam String name) {
        List<Station> stations = stationService.searchStationsByName(name);
        return ResponseEntity.ok(stations);
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Station>> getStationsByCity(@PathVariable String city) {
        List<Station> stations = stationService.getStationsByCity(city);
        return ResponseEntity.ok(stations);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Station> updateStation(@PathVariable Long id, @RequestBody Station station) {
        Station updatedStation = stationService.updateStation(id, station);
        return ResponseEntity.ok(updatedStation);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}
