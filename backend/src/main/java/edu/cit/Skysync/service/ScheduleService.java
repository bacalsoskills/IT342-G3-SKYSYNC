package edu.cit.Skysync.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.event.ScheduleCreatedEvent;
import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.repository.ScheduleRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ApplicationEventPublisher eventPublisher;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        NotificationRepository notificationRepository,
        UserService userService,
        ActivityService activityService,
        ApplicationEventPublisher eventPublisher
    ) {
        this.scheduleRepository = scheduleRepository;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.activityService = activityService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ScheduleEntity scheduleActivity(Long userId, Long activityId,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime) {
        UserEntity user = userService.getUserById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        ActivityEntity activity = activityService.getUserActivities(user).stream()
            .filter(a -> a.getActivityId().equals(activityId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Activity not found for user"));

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setUser(user);
        schedule.setActivity(activity);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);

        // Publish the schedule created event
        eventPublisher.publishEvent(new ScheduleCreatedEvent(savedSchedule));

        return savedSchedule;
    }

    public List<ScheduleEntity> getUserSchedules(Long userId) {
        return scheduleRepository.findByUser(
            userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

    public List<ScheduleEntity> getSchedulesBetweenDates(Long userId,
                                                          LocalDateTime start,
                                                          LocalDateTime end) {
        return scheduleRepository.findByUserAndStartTimeBetween(
            userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")),
            start,
            end
        );
    }

    public List<ScheduleEntity> getSchedulesByDayOfWeek(Long userId, String dayOfWeek) {
        return scheduleRepository.findByUserAndDayOfWeek(
            userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")),
            dayOfWeek
        );
    }

    @Transactional
    public boolean deleteScheduleById(Long scheduleId) {
        if (scheduleRepository.existsById(scheduleId)) {
            // Delete associated notifications first
            notificationRepository.deleteBySchedule_ScheduleId(scheduleId);

            // Then delete the schedule
            scheduleRepository.deleteById(scheduleId);
            return true;
        }
        return false;
    }
}