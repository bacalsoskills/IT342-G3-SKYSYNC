package edu.cit.Skysync.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.event.ScheduleCreatedEvent;
import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.repository.ScheduleRepository;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final ActivityService activityService;
    private final ApplicationEventPublisher eventPublisher;
    private final Scheduler scheduler;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        NotificationRepository notificationRepository,
        UserService userService,
        ActivityService activityService,
        ApplicationEventPublisher eventPublisher,
        @Qualifier("schedulerFactoryBean") Scheduler scheduler // Specify the bean to inject
    ) {
        this.scheduleRepository = scheduleRepository;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.activityService = activityService;
        this.eventPublisher = eventPublisher;
        this.scheduler = scheduler;
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

        // Create a Quartz job for the notification
        createNotificationJob(savedSchedule);

        return savedSchedule;
    }

    private void createNotificationJob(ScheduleEntity schedule) {
        try {
            // Create a JobDetail for the notification
            JobDetail jobDetail = JobBuilder.newJob(NotificationJob.class)
                .withIdentity("NotificationJob_" + schedule.getScheduleId(), "Notifications")
                .usingJobData("userId", schedule.getUser().getId())
                .usingJobData("message", "Reminder: " + schedule.getActivity().getName() + " starts at " + schedule.getStartTime())
                .build();

            // Create a Trigger for the job
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("NotificationTrigger_" + schedule.getScheduleId(), "Notifications")
                .startAt(java.sql.Timestamp.valueOf(schedule.getStartTime())) // Use the schedule's start time
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to schedule notification job", e);
        }
    }

    @Transactional
    public ScheduleEntity editSchedule(Long scheduleId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        // Fetch the existing schedule
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Update the schedule's start and end times
        schedule.setStartTime(newStartTime);
        schedule.setEndTime(newEndTime);

        // Save the updated schedule
        ScheduleEntity updatedSchedule = scheduleRepository.save(schedule);

        // Update the notification
        List<NotificationEntity> notifications = notificationRepository.findBySchedule_ScheduleId(scheduleId);
        if (notifications.isEmpty()) {
            throw new RuntimeException("Notification not found for schedule");
        }
        NotificationEntity notification = notifications.get(0);
        notification.setTriggerTime(newStartTime);
        notification.setMessage("Reminder: " + schedule.getActivity().getName() + " starts at " + newStartTime);
        notificationRepository.save(notification);

        // Reschedule the Quartz job
        rescheduleNotificationJob(updatedSchedule);

        return updatedSchedule;
    }

    private void rescheduleNotificationJob(ScheduleEntity schedule) {
        try {
            // Define the new trigger
            Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity("NotificationTrigger_" + schedule.getScheduleId(), "Notifications")
                .startAt(java.sql.Timestamp.valueOf(schedule.getStartTime())) // Use the updated start time
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

            // Reschedule the job with the new trigger
            scheduler.rescheduleJob(
                new TriggerKey("NotificationTrigger_" + schedule.getScheduleId(), "Notifications"),
                newTrigger
            );
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to reschedule notification job", e);
        }
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