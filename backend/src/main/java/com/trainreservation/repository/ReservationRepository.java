package com.trainreservation.repository;

import com.trainreservation.entity.Booking;
import com.trainreservation.entity.Reservation;
import com.trainreservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByBooking(Booking booking);
    
    Optional<Reservation> findBySeat(Seat seat);
    
    @Query("SELECT r FROM Reservation r WHERE r.booking.id = :bookingId")
    List<Reservation> findByBookingId(@Param("bookingId") Long bookingId);
    
    @Query("SELECT r FROM Reservation r WHERE r.booking.pnrNumber = :pnrNumber")
    List<Reservation> findByPnrNumber(@Param("pnrNumber") String pnrNumber);
    
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.booking.id = :bookingId")
    Long countPassengersByBookingId(@Param("bookingId") Long bookingId);
}
