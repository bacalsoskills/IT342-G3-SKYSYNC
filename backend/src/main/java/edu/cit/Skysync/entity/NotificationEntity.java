package edu.cit.Skysync.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private boolean isDelivered = false;

    // Relationship to User (many notifications can belong to one user)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Relationship to Schedule (optional, nullable)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "schedule_id", nullable = true)
    private ScheduleEntity schedule;

    // Utility method to mark the notification as read
    public void markAsRead() {
        this.isRead = true;
    }

    // Utility method to check if the notification is upcoming
    public boolean isUpcoming() {
        return triggerTime.isAfter(LocalDateTime.now());
    }
}