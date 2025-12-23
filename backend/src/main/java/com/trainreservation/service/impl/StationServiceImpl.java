package com.trainreservation.service.impl;

import com.trainreservation.dto.response.StationResponse;
import com.trainreservation.entity.Station;
import com.trainreservation.repository.StationRepository;
import com.trainreservation.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StationServiceImpl implements StationService {
    
    private final StationRepository stationRepository;
    
    @Override
    public Station createStation(Station station) {
        if (stationRepository.existsByStationCode(station.getStationCode())) {
            throw new RuntimeException("Station code already exists: " + station.getStationCode());
        }
        return stationRepository.save(station);
    }
    
    @Override
    public Station updateStation(Long id, Station station) {
        Station existingStation = stationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
        
        existingStation.setStationName(station.getStationName());
        existingStation.setCity(station.getCity());
        existingStation.setState(station.getState());
        existingStation.setPincode(station.getPincode());
        
        return stationRepository.save(existingStation);
    }
    
    @Override
    public void deleteStation(Long id) {
        if (!stationRepository.existsById(id)) {
            throw new RuntimeException("Station not found with id: " + id);
        }
        stationRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Station> getStationById(Long id) {
        return stationRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Station> getStationByCode(String stationCode) {
        return stationRepository.findByStationCode(stationCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<StationResponse> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private StationResponse mapToResponse(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .stationCode(station.getStationCode())
                .stationName(station.getStationName())
                .city(station.getCity())
                .state(station.getState())
                .pincode(station.getPincode())
                .build();
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Station> searchStationsByName(String name) {
        return stationRepository.findByStationNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Station> getStationsByCity(String city) {
        return stationRepository.findByCity(city);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByStationCode(String stationCode) {
        return stationRepository.existsByStationCode(stationCode);
    }
}
