package edu.cit.Skysync.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.event.ScheduleCreatedEvent;
import edu.cit.Skysync.repository.ScheduleRepository;
import jakarta.transaction.Transactional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ApplicationEventPublisher eventPublisher;
    private final PlatformTransactionManager transactionManager;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        UserService userService,
        ActivityService activityService,
        ApplicationEventPublisher eventPublisher,
        PlatformTransactionManager transactionManager // Add this
    ) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
        this.activityService = activityService;
        this.eventPublisher = eventPublisher;
        this.transactionManager = transactionManager;
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
    
    // Add this line to actually publish the event
    eventPublisher.publishEvent(new ScheduleCreatedEvent(savedSchedule));
    
    return savedSchedule;
}

    // Add a method to find schedules by day of week
    public List<ScheduleEntity> getSchedulesByDayOfWeek(Long userId, String dayOfWeek) {
        return scheduleRepository.findByUserAndDayOfWeek(
            userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")),
            dayOfWeek
        );
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
}