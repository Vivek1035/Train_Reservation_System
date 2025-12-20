package com.trainreservation.util;

import com.trainreservation.dto.response.*;
import com.trainreservation.entity.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
    
    // Train mappings
    public TrainResponse toTrainResponse(Train train) {
        return TrainResponse.builder()
            .id(train.getId())
            .trainNumber(train.getTrainNumber())
            .trainName(train.getTrainName())
            .sourceStation(toStationBasicResponse(train.getSourceStation()))
            .destinationStation(toStationBasicResponse(train.getDestinationStation()))
            .departureTime(train.getDepartureTime())
            .arrivalTime(train.getArrivalTime())
            .totalSeats(train.getTotalSeats())
            .availableSeats(train.getAvailableSeats())
            .baseFare(train.getBaseFare())
            .active(train.getActive())
            .operatingDays(train.getOperatingDays())
            .coaches(train.getCoaches() != null ? 
                train.getCoaches().stream()
                    .map(this::toCoachResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }
    
    public TrainSearchResponse toTrainSearchResponse(Train train) {
        return TrainSearchResponse.builder()
            .id(train.getId())
            .trainNumber(train.getTrainNumber())
            .trainName(train.getTrainName())
            .sourceStationCode(train.getSourceStation().getStationCode())
            .sourceStationName(train.getSourceStation().getStationName())
            .destinationStationCode(train.getDestinationStation().getStationCode())
            .destinationStationName(train.getDestinationStation().getStationName())
            .departureTime(train.getDepartureTime())
            .arrivalTime(train.getArrivalTime())
            .duration(calculateDuration(train.getDepartureTime(), train.getArrivalTime()))
            .availableSeats(train.getAvailableSeats())
            .startingFare(train.getBaseFare())
            .hasAvailability(train.getAvailableSeats() > 0)
            .build();
    }
    
    private TrainResponse.CoachResponse toCoachResponse(Coach coach) {
        return TrainResponse.CoachResponse.builder()
            .id(coach.getId())
            .coachNumber(coach.getCoachNumber())
            .coachType(coach.getCoachType())
            .coachTypeDescription(coach.getCoachType().getDescription())
            .totalSeats(coach.getTotalSeats())
            .availableSeats(coach.getAvailableSeats())
            .fareMultiplier(coach.getFareMultiplier())
            .calculatedFare(coach.getTrain().getBaseFare().multiply(coach.getFareMultiplier()))
            .build();
    }
    
    // Booking mappings
    public BookingResponse toBookingResponse(Booking booking) {
        return BookingResponse.builder()
            .id(booking.getId())
            .pnrNumber(booking.getPnrNumber())
            .status(booking.getStatus())
            .journeyDate(booking.getJourneyDate())
            .numberOfPassengers(booking.getNumberOfPassengers())
            .totalFare(booking.getTotalFare())
            .remarks(booking.getRemarks())
            .bookedAt(booking.getCreatedAt())
            .train(toTrainBasicInfo(booking.getTrain()))
            .reservations(booking.getReservations() != null ?
                booking.getReservations().stream()
                    .map(this::toReservationInfo)
                    .collect(Collectors.toList()) : null)
            .payment(booking.getPayment() != null ? 
                toPaymentInfo(booking.getPayment()) : null)
            .build();
    }
    
    public BookingHistoryResponse toBookingHistoryResponse(Booking booking) {
        boolean isPast = booking.getJourneyDate().isBefore(LocalDate.now());
        boolean canCancel = !isPast && 
            (booking.getStatus().name().equals("CONFIRMED") || 
             booking.getStatus().name().equals("PENDING"));
        
        String route = booking.getTrain().getSourceStation().getStationName() + 
                      " -> " + 
                      booking.getTrain().getDestinationStation().getStationName();
        
        return BookingHistoryResponse.builder()
            .id(booking.getId())
            .pnrNumber(booking.getPnrNumber())
            .status(booking.getStatus())
            .journeyDate(booking.getJourneyDate())
            .numberOfPassengers(booking.getNumberOfPassengers())
            .totalFare(booking.getTotalFare())
            .bookedAt(booking.getCreatedAt())
            .trainNumber(booking.getTrain().getTrainNumber())
            .trainName(booking.getTrain().getTrainName())
            .route(route)
            .canCancel(canCancel)
            .isPastJourney(isPast)
            .build();
    }
    
    private BookingResponse.TrainBasicInfo toTrainBasicInfo(Train train) {
        return BookingResponse.TrainBasicInfo.builder()
            .id(train.getId())
            .trainNumber(train.getTrainNumber())
            .trainName(train.getTrainName())
            .sourceStation(train.getSourceStation().getStationName())
            .destinationStation(train.getDestinationStation().getStationName())
            .build();
    }
    
    private BookingResponse.ReservationInfo toReservationInfo(Reservation reservation) {
        return BookingResponse.ReservationInfo.builder()
            .id(reservation.getId())
            .passengerName(reservation.getPassengerName())
            .passengerAge(reservation.getPassengerAge())
            .passengerGender(reservation.getPassengerGender())
            .quotaType(reservation.getQuotaType())
            .coachNumber(reservation.getSeat().getCoach().getCoachNumber())
            .seatNumber(reservation.getSeat().getSeatNumber())
            .fare(reservation.getFare())
            .build();
    }
    
    private BookingResponse.PaymentInfo toPaymentInfo(Payment payment) {
        return BookingResponse.PaymentInfo.builder()
            .id(payment.getId())
            .transactionId(payment.getTransactionId())
            .amount(payment.getAmount())
            .paymentMethod(payment.getPaymentMethod().name())
            .status(payment.getStatus().name())
            .paymentDate(payment.getPaymentDate())
            .build();
    }
    
    // Station mappings
    public StationBasicResponse toStationBasicResponse(Station station) {
        return StationBasicResponse.builder()
            .id(station.getId())
            .stationCode(station.getStationCode())
            .stationName(station.getStationName())
            .city(station.getCity())
            .state(station.getState())
            .build();
    }
    
    public StationResponse toStationResponse(Station station) {
        return StationResponse.builder()
            .id(station.getId())
            .stationCode(station.getStationCode())
            .stationName(station.getStationName())
            .city(station.getCity())
            .state(station.getState())
            .pincode(station.getPincode())
            .build();
    }
    
    // User mappings
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phoneNumber(user.getPhoneNumber())
            .address(user.getAddress())
            .role(user.getRole())
            .active(user.getActive())
            .createdAt(user.getCreatedAt())
            .build();
    }
    
    // Utility methods
    private String calculateDuration(LocalTime departure, LocalTime arrival) {
        Duration duration;
        if (arrival.isBefore(departure)) {
            // Next day arrival
            duration = Duration.between(departure, arrival.plusHours(24));
        } else {
            duration = Duration.between(departure, arrival);
        }
        
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%d hrs %d mins", hours, minutes);
    }
}
