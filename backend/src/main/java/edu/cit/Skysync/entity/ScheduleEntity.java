package edu.cit.Skysync.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "schedule_entity")
@Data
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;
    
    @Column(nullable = false)
    private LocalDate date;  // New field for the date
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @ManyToOne
    @JoinColumn(name = "activity_id", referencedColumnName = "activity_id")
    private ActivityEntity activity;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now();  // Automatically set date on creation
    }
}