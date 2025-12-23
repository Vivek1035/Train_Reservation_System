package com.trainreservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trains", indexes = {
    @Index(name = "idx_train_number", columnList = "train_number", unique = true),
    @Index(name = "idx_train_route", columnList = "source_station_id, destination_station_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "train_number", unique = true, nullable = false, length = 10)
    private String trainNumber;
    
    @Column(nullable = false)
    private String trainName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_station_id", nullable = false)
    private Station destinationStation;
    
    @Column(nullable = false)
    private LocalTime departureTime;
    
    @Column(nullable = false)
    private LocalTime arrivalTime;
    
    @Column(nullable = false)
    private Integer totalSeats;
    
    @Column(nullable = false)
    private Integer availableSeats;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseFare;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    // Days of operation (bit mask: Mon=1, Tue=2, Wed=4... or JSON string)
    @Column(columnDefinition = "TEXT")
    private String operatingDays; // e.g., "MON,TUE,WED,THU,FRI,SAT,SUN"
    
    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Coach> coaches = new ArrayList<>();
    
    @OneToMany(mappedBy = "train")
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
