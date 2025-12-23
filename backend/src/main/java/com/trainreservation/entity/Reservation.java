package com.trainreservation.entity;

import com.trainreservation.enums.QuotaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations", indexes = {
    @Index(name = "idx_reservation_booking", columnList = "booking_id"),
    @Index(name = "idx_reservation_seat", columnList = "seat_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    
    // Passenger details
    @Column(nullable = false)
    private String passengerName;
    
    @Column(nullable = false)
    private Integer passengerAge;
    
    @Column(nullable = false, length = 10)
    private String passengerGender; // MALE, FEMALE, OTHER
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaType quotaType;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fare;
    
    @Column(columnDefinition = "TEXT")
    private String specialRequests;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
