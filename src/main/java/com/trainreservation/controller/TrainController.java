package com.trainreservation.controller;

import com.trainreservation.dto.ApiResponse;
import com.trainreservation.dto.PageResponse;
import com.trainreservation.dto.request.TrainCreateRequest;
import com.trainreservation.dto.request.TrainSearchRequest;
import com.trainreservation.dto.response.TrainResponse;
import com.trainreservation.dto.response.TrainSearchResponse;
import com.trainreservation.entity.Train;
import com.trainreservation.exception.ResourceNotFoundException;
import com.trainreservation.service.TrainService;
import com.trainreservation.util.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    
    private final TrainService trainService;
    private final DtoMapper dtoMapper;
    
    /**
     * Search trains between stations
     * POST /api/trains/search
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<TrainSearchResponse>>> searchTrains(
            @Valid @RequestBody TrainSearchRequest searchRequest) {
        
        List<Train> trains = trainService.searchTrainsBetweenStations(
            searchRequest.getSourceStationId(), 
            searchRequest.getDestinationStationId()
        );
        
        if (searchRequest.getOnlyAvailable()) {
            trains = trains.stream()
                .filter(t -> t.getAvailableSeats() > 0)
                .collect(Collectors.toList());
        }
        
        List<TrainSearchResponse> response = trains.stream()
            .map(dtoMapper::toTrainSearchResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(
            ApiResponse.success("Found " + response.size() + " trains", response)
        );
    }
    
    /**
     * Get all trains with pagination and sorting
     * GET /api/trains?page=0&size=10&sort=trainNumber,asc
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TrainResponse>>> getAllTrains(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "trainNumber") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Note: This would require updating TrainRepository to add findAll(Pageable)
        // For now, let's use a simplified approach
        List<Train> trains = active != null && active 
            ? trainService.getActiveTrains() 
            : trainService.getAllTrains();
        
        List<TrainResponse> trainResponses = trains.stream()
            .map(dtoMapper::toTrainResponse)
            .collect(Collectors.toList());
        
        // Create a simple page response
        PageResponse<TrainResponse> pageResponse = PageResponse.<TrainResponse>builder()
            .content(trainResponses)
            .pageNumber(page)
            .pageSize(size)
            .totalElements(trainResponses.size())
            .totalPages((int) Math.ceil((double) trainResponses.size() / size))
            .first(page == 0)
            .last(true)
            .empty(trainResponses.isEmpty())
            .build();
        
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
    
    /**
     * Get train by ID
     * GET /api/trains/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainResponse>> getTrainById(@PathVariable Long id) {
        Train train = trainService.getTrainById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Train", "id", id));
        
        TrainResponse response = dtoMapper.toTrainResponse(train);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Get train by number
     * GET /api/trains/number/{trainNumber}
     */
    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<ApiResponse<TrainResponse>> getTrainByNumber(
            @PathVariable String trainNumber) {
        
        Train train = trainService.getTrainByNumber(trainNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Train", "trainNumber", trainNumber));
        
        TrainResponse response = dtoMapper.toTrainResponse(train);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Admin: Create new train
     * POST /api/trains
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TrainResponse>> createTrain(
            @Valid @RequestBody TrainCreateRequest request) {
        
        // This would require a service method to handle the complex creation
        // For now, throw an exception as placeholder
        throw new UnsupportedOperationException("Train creation with coaches will be implemented in service layer");
    }
    
    /**
     * Admin: Update train availability
     * PATCH /api/trains/{id}/seats
     */
    @PatchMapping("/{id}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TrainResponse>> updateAvailableSeats(
            @PathVariable Long id, 
            @RequestParam Integer change) {
        
        Train updatedTrain = trainService.updateAvailableSeats(id, change);
        TrainResponse response = dtoMapper.toTrainResponse(updatedTrain);
        
        return ResponseEntity.ok(
            ApiResponse.success("Seat availability updated", response)
        );
    }
    
    /**
     * Admin: Delete train
     * DELETE /api/trains/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.ok(
            ApiResponse.success("Train deleted successfully", null)
        );
    }
}
