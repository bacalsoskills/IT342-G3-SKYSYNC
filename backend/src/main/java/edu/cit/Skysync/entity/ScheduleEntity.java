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
    private LocalDate date;
    
    @Column(nullable = false)
    private String dayOfWeek; // Stores day name (e.g., "Monday")
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private ActivityEntity activity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        LocalDate currentDate = LocalDate.now();
        this.date = currentDate;
        this.dayOfWeek = currentDate.getDayOfWeek()
                              .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.US);
        
        // Also adjust startTime and endTime to use current date
        if (this.startTime != null) {
            this.startTime = this.startTime.withYear(currentDate.getYear())
                                     .withMonth(currentDate.getMonthValue())
                                     .withDayOfMonth(currentDate.getDayOfMonth());
        }
        if (this.endTime != null) {
            this.endTime = this.endTime.withYear(currentDate.getYear())
                                 .withMonth(currentDate.getMonthValue())
                                 .withDayOfMonth(currentDate.getDayOfMonth());
        }
    }
}