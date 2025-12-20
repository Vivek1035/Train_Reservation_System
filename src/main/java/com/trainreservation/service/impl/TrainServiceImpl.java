package com.trainreservation.service.impl;

import com.trainreservation.entity.Train;
import com.trainreservation.repository.TrainRepository;
import com.trainreservation.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainServiceImpl implements TrainService {
    
    private final TrainRepository trainRepository;
    
    @Override
    public Train createTrain(Train train) {
        if (trainRepository.existsByTrainNumber(train.getTrainNumber())) {
            throw new RuntimeException("Train number already exists: " + train.getTrainNumber());
        }
        return trainRepository.save(train);
    }
    
    @Override
    public Train updateTrain(Long id, Train train) {
        Train existingTrain = trainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
        
        existingTrain.setTrainName(train.getTrainName());
        existingTrain.setDepartureTime(train.getDepartureTime());
        existingTrain.setArrivalTime(train.getArrivalTime());
        existingTrain.setBaseFare(train.getBaseFare());
        existingTrain.setOperatingDays(train.getOperatingDays());
        existingTrain.setActive(train.getActive());
        
        return trainRepository.save(existingTrain);
    }
    
    @Override
    public void deleteTrain(Long id) {
        if (!trainRepository.existsById(id)) {
            throw new RuntimeException("Train not found with id: " + id);
        }
        trainRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Train> getActiveTrains() {
        return trainRepository.findByActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Train> searchTrainsBetweenStations(Long sourceStationId, Long destinationStationId) {
        return trainRepository.findActiveTrainsBetweenStations(sourceStationId, destinationStationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Train> searchTrainsByName(String name) {
        return trainRepository.searchByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Train> getTrainsWithAvailableSeats() {
        return trainRepository.findTrainsWithAvailableSeats();
    }
    
    @Override
    public Train updateAvailableSeats(Long trainId, Integer seatsChange) {
        Train train = trainRepository.findById(trainId)
            .orElseThrow(() -> new RuntimeException("Train not found with id: " + trainId));
        
        int newAvailableSeats = train.getAvailableSeats() + seatsChange;
        if (newAvailableSeats < 0 || newAvailableSeats > train.getTotalSeats()) {
            throw new RuntimeException("Invalid seat count");
        }
        
        train.setAvailableSeats(newAvailableSeats);
        return trainRepository.save(train);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTrainNumber(String trainNumber) {
        return trainRepository.existsByTrainNumber(trainNumber);
    }
}
