package edu.cit.Skysync.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.entity.UserEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByUser(UserEntity user);
    List<ScheduleEntity> findByUserAndStartTimeBetween(UserEntity user, LocalDateTime start, LocalDateTime end);
    List<ScheduleEntity> findByUserAndDayOfWeek(UserEntity user, String dayOfWeek); // New method
    Optional<ScheduleEntity> findByActivity_ActivityId(Long activityId);
}