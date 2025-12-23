package com.trainreservation.entity;

import com.trainreservation.enums.CoachType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coaches", indexes = {
    @Index(name = "idx_coach_train", columnList = "train_id, coach_number")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coach {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    
    @Column(nullable = false, length = 10)
    private String coachNumber; // e.g., "A1", "B2", "S5"
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoachType coachType;
    
    @Column(nullable = false)
    private Integer totalSeats;
    
    @Column(nullable = false)
    private Integer availableSeats;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fareMultiplier; // Multiplier for base fare (e.g., 1.5 for AC, 1.0 for Sleeper)
    
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
