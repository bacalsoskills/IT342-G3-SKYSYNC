package edu.cit.Skysync.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.Skysync.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Find notifications by user ID
    List<NotificationEntity> findByUser_Id(Long userId);

    // Find notifications by schedule ID
    List<NotificationEntity> findBySchedule_ScheduleId(Long scheduleId);

    // Delete notifications by schedule ID
    void deleteBySchedule_ScheduleId(Long scheduleId);

    // Find unread notifications for a user
    List<NotificationEntity> findByUser_IdAndIsReadFalse(Long userId);

    // Find upcoming notifications for a user (triggerTime > now)
    List<NotificationEntity> findByUser_IdAndTriggerTimeAfter(Long userId, LocalDateTime now);

    // Find notifications within a specific time range for a user
    List<NotificationEntity> findByUser_IdAndTriggerTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
}