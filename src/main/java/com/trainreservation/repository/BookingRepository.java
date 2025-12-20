package com.trainreservation.repository;

import com.trainreservation.entity.Booking;
import com.trainreservation.entity.Train;
import com.trainreservation.entity.User;
import com.trainreservation.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByPnrNumber(String pnrNumber);
    
    List<Booking> findByUser(User user);
    
    List<Booking> findByUserOrderByCreatedAtDesc(User user);
    
    List<Booking> findByTrain(Train train);
    
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    List<Booking> findByUserAndStatus(@Param("userId") Long userId, 
                                       @Param("status") BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.train.id = :trainId AND b.journeyDate = :journeyDate")
    List<Booking> findByTrainAndJourneyDate(@Param("trainId") Long trainId, 
                                             @Param("journeyDate") LocalDate journeyDate);
    
    @Query("SELECT b FROM Booking b WHERE b.journeyDate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.train.id = :trainId " +
           "AND b.journeyDate = :journeyDate AND b.status = 'CONFIRMED'")
    Long countConfirmedBookingsForTrainOnDate(@Param("trainId") Long trainId, 
                                                @Param("journeyDate") LocalDate journeyDate);
    
    boolean existsByPnrNumber(String pnrNumber);
}
