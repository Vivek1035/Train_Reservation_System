package com.trainreservation.service.impl;

import com.trainreservation.dto.ApiResponse;
import com.trainreservation.dto.request.BookingCreateRequest;
import com.trainreservation.dto.response.BookingResponse;
import com.trainreservation.entity.User;
import com.trainreservation.entity.Train;
import com.trainreservation.entity.Seat;
import com.trainreservation.entity.Reservation;
import com.trainreservation.entity.Booking;
import com.trainreservation.enums.BookingStatus;
import com.trainreservation.repository.BookingRepository;
import com.trainreservation.repository.UserRepository;
import com.trainreservation.repository.TrainRepository;
import com.trainreservation.repository.SeatRepository;
import com.trainreservation.repository.ReservationRepository;
import com.trainreservation.service.BookingService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Booking createBooking(BookingCreateRequest request) {

        // 1️. Load user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️. Load train
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        int passengerCount = request.getPassengers().size();
        BigDecimal totalFare = train.getBaseFare()
                .multiply(BigDecimal.valueOf(passengerCount));

        // 3️. Create booking
        Booking booking = Booking.builder()
                .user(user)
                .train(train)
                .journeyDate(request.getJourneyDate())
                .numberOfPassengers(passengerCount)
                .totalFare(totalFare)
                .status(BookingStatus.PENDING)
                .pnrNumber(generatePnrNumber())
                .createdAt(LocalDateTime.now())
                .build();

        booking = bookingRepository.save(booking);

        // 4️. Create passengers & lock seats
        for (BookingCreateRequest.PassengerRequest p : request.getPassengers()) {

            Seat seat = seatRepository.findById(p.getCoachId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));

            if (!seat.getAvailable()) {
                throw new RuntimeException("Seat already booked");
            }

            seat.setAvailable(false);
            seatRepository.save(seat);

            Reservation reservation = Reservation.builder()
                    .booking(booking)
                    .passengerName(p.getPassengerName())
                    .passengerAge(p.getPassengerAge())
                    .passengerGender(p.getPassengerGender())
                    .quotaType(p.getQuotaType())
                    .fare(train.getBaseFare())
                    .seat(seat)
                    .build();

            reservationRepository.save(reservation);
        }

        return booking;
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserOrderByCreatedAtDesc(user);
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
