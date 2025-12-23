package com.trainreservation.service;

import com.trainreservation.entity.Train;

import java.util.List;
import java.util.Optional;

public interface TrainService {
    
    Train createTrain(Train train);
    
    Train updateTrain(Long id, Train train);
    
    void deleteTrain(Long id);
    
    Optional<Train> getTrainById(Long id);
    
    Optional<Train> getTrainByNumber(String trainNumber);
    
    List<Train> getAllTrains();
    
    List<Train> getActiveTrains();
    
    List<Train> searchTrainsBetweenStations(Long sourceStationId, Long destinationStationId);
    
    List<Train> searchTrainsByName(String name);
    
    List<Train> getTrainsWithAvailableSeats();
    
    Train updateAvailableSeats(Long trainId, Integer seatsChange);
    
    boolean existsByTrainNumber(String trainNumber);
}
