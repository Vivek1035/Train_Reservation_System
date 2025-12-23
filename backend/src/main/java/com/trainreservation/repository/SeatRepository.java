package com.trainreservation.repository;

import com.trainreservation.entity.Coach;
import com.trainreservation.entity.Seat;
import com.trainreservation.enums.QuotaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByCoach(Coach coach);

    List<Seat> findByCoachAndAvailableTrue(Coach coach);

    List<Seat> findByCoachAndQuotaType(Coach coach, QuotaType quotaType);

    @Query("SELECT s FROM Seat s WHERE s.coach.id = :coachId AND s.available = true AND s.quotaType = :quotaType")
    List<Seat> findAvailableSeatsByQuota(@Param("coachId") Long coachId,
            @Param("quotaType") QuotaType quotaType);

    @Query("""
                SELECT s
                FROM Seat s
                WHERE s.coach.train.id = :trainId
                  AND s.available = true
                ORDER BY CAST(s.seatNumber AS integer)
            """)
    List<Seat> findAvailableSeatsForTrain(@Param("trainId") Long trainId);

    Optional<Seat> findByCoachAndSeatNumber(Coach coach, String seatNumber);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.coach.id = :coachId AND s.available = true")
    Long countAvailableSeats(@Param("coachId") Long coachId);
}
