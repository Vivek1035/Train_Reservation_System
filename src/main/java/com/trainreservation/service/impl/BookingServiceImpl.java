package com.trainreservation.service.impl;

import com.trainreservation.entity.Booking;
import com.trainreservation.enums.BookingStatus;
import com.trainreservation.repository.BookingRepository;
import com.trainreservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    
    private final BookingRepository bookingRepository;
    
    @Override
    public Booking createBooking(Booking booking) {
        booking.setPnrNumber(generatePnrNumber());
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }
    
    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking existingBooking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        existingBooking.setStatus(booking.getStatus());
        existingBooking.setRemarks(booking.getRemarks());
        
        return bookingRepository.save(existingBooking);
    }
    
    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingByPnr(String pnrNumber) {
        return bookingRepository.findByPnrNumber(pnrNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserAndStatus(userId, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByTrainId(Long trainId) {
        return bookingRepository.findAll().stream()
            .filter(b -> b.getTrain().getId().equals(trainId))
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    
    @Override
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
    
    @Override
    public Booking confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsForTrainOnDate(Long trainId, LocalDate journeyDate) {
        return bookingRepository.findByTrainAndJourneyDate(trainId, journeyDate);
    }
    
    @Override
    public String generatePnrNumber() {
        Random random = new Random();
        String pnr;
        do {
            pnr = String.format("%010d", random.nextInt(1000000000));
        } while (bookingRepository.existsByPnrNumber(pnr));
        return pnr;
    }
}
