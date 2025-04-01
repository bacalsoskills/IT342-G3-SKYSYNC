package edu.cit.Skysync.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.Skysync.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
    // Find by user ID (using the 'user' relationship field)
    List<NotificationEntity> findByUser_Id(Long userId);
    
    // Optional: Find by schedule ID (if you added schedule_id column)
    List<NotificationEntity> findByScheduleId(Long scheduleId);
    
    // Find unread notifications for a user
    List<NotificationEntity> findByUser_IdAndIsReadFalse(Long userId);
    
    // Find upcoming notifications (triggerTime > now)
    List<NotificationEntity> findByUser_IdAndTriggerTimeAfter(Long userId, LocalDateTime now);
}