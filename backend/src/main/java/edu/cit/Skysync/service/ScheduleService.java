package edu.cit.Skysync.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.ScheduleRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final ActivityService activityService;

    public ScheduleService(ScheduleRepository scheduleRepository, 
                         UserService userService,
                         ActivityService activityService) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
        this.activityService = activityService;
    }

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
        // Date is automatically set by @PrePersist
        
        return scheduleRepository.save(schedule);
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