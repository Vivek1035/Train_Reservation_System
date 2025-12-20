package com.trainreservation.controller;

import com.trainreservation.dto.ApiResponse;
import com.trainreservation.dto.PageResponse;
import com.trainreservation.dto.request.BookingCreateRequest;
import com.trainreservation.dto.response.BookingHistoryResponse;
import com.trainreservation.dto.response.BookingResponse;
import com.trainreservation.entity.Booking;
import com.trainreservation.enums.BookingStatus;
import com.trainreservation.exception.ResourceNotFoundException;
import com.trainreservation.service.BookingService;
import com.trainreservation.util.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    
    private final BookingService bookingService;
    private final DtoMapper dtoMapper;
    
    /**
     * Create new booking
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody BookingCreateRequest request) {
        
        // This would require a comprehensive service method to:
        // 1. Validate train and user exist
        // 2. Check seat availability
        // 3. Allocate seats for each passenger
        // 4. Calculate total fare
        // 5. Create booking, reservations, and payment records
        // For now, throw placeholder
        throw new UnsupportedOperationException(
            "Booking creation will be implemented in service layer with seat allocation logic"
        );
    }
    
    /**
     * Get booking by ID
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        
        BookingResponse response = dtoMapper.toBookingResponse(booking);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Get booking by PNR number
     * GET /api/bookings/pnr/{pnr}
     */
    @GetMapping("/pnr/{pnr}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingByPnr(@PathVariable String pnr) {
        Booking booking = bookingService.getBookingByPnr(pnr)
            .orElseThrow(() -> new ResourceNotFoundException("Booking", "PNR", pnr));
        
        BookingResponse response = dtoMapper.toBookingResponse(booking);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Get user booking history
     * GET /api/bookings/user/{userId}/history?page=0&size=10
     */
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<ApiResponse<PageResponse<BookingHistoryResponse>>> getUserBookingHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        
        // Sort by booking date descending (most recent first)
        bookings.sort((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()));
        
        List<BookingHistoryResponse> historyResponses = bookings.stream()
            .map(dtoMapper::toBookingHistoryResponse)
            .collect(Collectors.toList());
        
        // Simple pagination
        int start = page * size;
        int end = Math.min(start + size, historyResponses.size());
        List<BookingHistoryResponse> pagedContent = start < historyResponses.size() 
            ? historyResponses.subList(start, end) 
            : List.of();
        
        PageResponse<BookingHistoryResponse> pageResponse = PageResponse.<BookingHistoryResponse>builder()
            .content(pagedContent)
            .pageNumber(page)
            .pageSize(size)
            .totalElements(historyResponses.size())
            .totalPages((int) Math.ceil((double) historyResponses.size() / size))
            .first(page == 0)
            .last(end >= historyResponses.size())
            .empty(pagedContent.isEmpty())
            .build();
        
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
    
    /**
     * Get user's active bookings (upcoming journeys)
     * GET /api/bookings/user/{userId}/active
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<ApiResponse<List<BookingHistoryResponse>>> getUserActiveBookings(
            @PathVariable Long userId) {
        
        List<Booking> bookings = bookingService.getBookingsByUserId(userId);
        
        // Filter only future bookings with confirmed/pending status
        List<BookingHistoryResponse> activeBookings = bookings.stream()
            .filter(b -> b.getJourneyDate().isAfter(LocalDate.now()) || 
                        b.getJourneyDate().equals(LocalDate.now()))
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || 
                        b.getStatus() == BookingStatus.PENDING)
            .map(dtoMapper::toBookingHistoryResponse)
            .sorted((b1, b2) -> b1.getJourneyDate().compareTo(b2.getJourneyDate()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(
            ApiResponse.success("Found " + activeBookings.size() + " active bookings", activeBookings)
        );
    }
    
    /**
     * Get bookings by status
     * GET /api/bookings/status/{status}?page=0&size=20
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByStatus(
            @PathVariable BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        
        List<BookingResponse> responses = bookings.stream()
            .map(dtoMapper::toBookingResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(
            ApiResponse.success("Found " + responses.size() + " bookings with status: " + status, responses)
        );
    }
    
    /**
     * Cancel booking
     * PATCH /api/bookings/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long id) {
        Booking cancelledBooking = bookingService.cancelBooking(id);
        BookingResponse response = dtoMapper.toBookingResponse(cancelledBooking);
        
        return ResponseEntity.ok(
            ApiResponse.success("Booking cancelled successfully", response)
        );
    }
    
    /**
     * Confirm booking (admin/payment gateway)
     * PATCH /api/bookings/{id}/confirm
     */
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(@PathVariable Long id) {
        Booking confirmedBooking = bookingService.confirmBooking(id);
        BookingResponse response = dtoMapper.toBookingResponse(confirmedBooking);
        
        return ResponseEntity.ok(
            ApiResponse.success("Booking confirmed successfully", response)
        );
    }
    
    /**
     * Admin: Get all bookings for a train on a specific date
     * GET /api/bookings/train/{trainId}/date/{date}
     */
    @GetMapping("/train/{trainId}/date/{date}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsForTrainOnDate(
            @PathVariable Long trainId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<Booking> bookings = bookingService.getBookingsForTrainOnDate(trainId, date);
        
        List<BookingResponse> responses = bookings.stream()
            .map(dtoMapper::toBookingResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(
            ApiResponse.success("Found " + responses.size() + " bookings", responses)
        );
    }
}
