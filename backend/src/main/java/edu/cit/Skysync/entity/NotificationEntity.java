package edu.cit.Skysync.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "notification_entity")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime triggerTime;
    
    @Column(nullable = false)
    private boolean isRead = false;
    
    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered = false;  // Initialize with default value
    
    
    // Relationship to User (many notifications can belong to one user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    // Optional: Reference to Schedule without creating a hard dependency
    @Column(name = "schedule_id")
    private Long scheduleId; // Stores ID only, not full entity
    
    // Recommended: Add these utility methods
    public void markAsRead() {
        this.isRead = true;
    }
    
    public boolean isUpcoming() {
        return triggerTime.isAfter(LocalDateTime.now());
    }
}