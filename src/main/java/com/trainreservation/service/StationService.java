package com.trainreservation.service;

import com.trainreservation.entity.Station;

import java.util.List;
import java.util.Optional;

public interface StationService {
    
    Station createStation(Station station);
    
    Station updateStation(Long id, Station station);
    
    void deleteStation(Long id);
    
    Optional<Station> getStationById(Long id);
    
    Optional<Station> getStationByCode(String stationCode);
    
    List<Station> getAllStations();
    
    List<Station> searchStationsByName(String name);
    
    List<Station> getStationsByCity(String city);
    
    boolean existsByStationCode(String stationCode);
}
