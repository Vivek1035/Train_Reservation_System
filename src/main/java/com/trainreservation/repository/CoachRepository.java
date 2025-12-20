package com.trainreservation.repository;

import com.trainreservation.entity.Coach;
import com.trainreservation.entity.Train;
import com.trainreservation.enums.CoachType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    
    List<Coach> findByTrain(Train train);
    
    List<Coach> findByTrainAndCoachType(Train train, CoachType coachType);
    
    Optional<Coach> findByTrainAndCoachNumber(Train train, String coachNumber);
    
    @Query("SELECT c FROM Coach c WHERE c.train.id = :trainId AND c.availableSeats > 0")
    List<Coach> findCoachesWithAvailableSeats(@Param("trainId") Long trainId);
    
    @Query("SELECT c FROM Coach c WHERE c.train.id = :trainId AND c.coachType = :coachType AND c.availableSeats > 0")
    List<Coach> findAvailableCoachesByType(@Param("trainId") Long trainId, 
                                            @Param("coachType") CoachType coachType);
}
