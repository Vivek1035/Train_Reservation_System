package com.trainreservation.service;

import com.trainreservation.dto.request.BookingCreateRequest;
import com.trainreservation.entity.Booking;
import com.trainreservation.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    
    Booking createBooking(BookingCreateRequest request);
    
    Booking updateBooking(Long id, Booking booking);
    
    void deleteBooking(Long id);
    
    Optional<Booking> getBookingById(Long id);
    
    Optional<Booking> getBookingByPnr(String pnrNumber);
    
    List<Booking> getBookingsByUserId(Long userId);
    
    List<Booking> getBookingsByTrainId(Long trainId);
    
    List<Booking> getBookingsByStatus(BookingStatus status);
    
    Booking cancelBooking(Long id);
    
    Booking confirmBooking(Long id);
    
    List<Booking> getBookingsForTrainOnDate(Long trainId, LocalDate journeyDate);
    
    String generatePnrNumber();
}
