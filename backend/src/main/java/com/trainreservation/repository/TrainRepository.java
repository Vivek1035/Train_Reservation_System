package com.trainreservation.repository;

import com.trainreservation.entity.Train;
import com.trainreservation.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    
    Optional<Train> findByTrainNumber(String trainNumber);
    
    List<Train> findByActiveTrue();
    
    List<Train> findBySourceStationAndDestinationStation(Station sourceStation, Station destinationStation);
    
    @Query("SELECT t FROM Train t WHERE t.sourceStation.id = :sourceId " +
           "AND t.destinationStation.id = :destinationId AND t.active = true")
    List<Train> findActiveTrainsBetweenStations(@Param("sourceId") Long sourceId, 
                                                 @Param("destinationId") Long destinationId);
    
    @Query("SELECT t FROM Train t WHERE t.trainName LIKE %:name% AND t.active = true")
    List<Train> searchByName(@Param("name") String name);
    
    @Query("SELECT t FROM Train t WHERE t.availableSeats > 0 AND t.active = true")
    List<Train> findTrainsWithAvailableSeats();
    
    boolean existsByTrainNumber(String trainNumber);
}
