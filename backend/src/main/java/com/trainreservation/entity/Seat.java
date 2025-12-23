package com.trainreservation.entity;

import com.trainreservation.enums.QuotaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats", indexes = {
    @Index(name = "idx_seat_coach", columnList = "coach_id, seat_number"),
    @Index(name = "idx_seat_availability", columnList = "available")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;
    
    @Column(nullable = false, length = 5)
    private String seatNumber; // e.g., "1", "2", "3A", "4B"
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuotaType quotaType;
    
    // Seat preferences
    private Boolean isWindowSeat = false;
    private Boolean isAisleSeat = false;
    private Boolean isLowerBerth = false;
    private Boolean isUpperBerth = false;
    
    @OneToOne(mappedBy = "seat")
    private Reservation reservation;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
