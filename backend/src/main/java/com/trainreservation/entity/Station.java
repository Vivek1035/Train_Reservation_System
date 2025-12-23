package com.trainreservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stations", indexes = {
    @Index(name = "idx_station_code", columnList = "station_code", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "station_code", unique = true, nullable = false, length = 10)
    private String stationCode;
    
    @Column(nullable = false)
    private String stationName;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(length = 10)
    private String pincode;
    
    @OneToMany(mappedBy = "sourceStation")
    @Builder.Default
    private List<Train> trainsFromHere = new ArrayList<>();
    
    @OneToMany(mappedBy = "destinationStation")
    @Builder.Default
    private List<Train> trainsToHere = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
