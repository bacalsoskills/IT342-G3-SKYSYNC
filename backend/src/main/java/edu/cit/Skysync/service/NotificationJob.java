package edu.cit.Skysync.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.service.UserService;

import java.time.LocalDateTime;

@Component
public class NotificationJob implements Job {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            // Retrieve job data
            Long userId = context.getJobDetail().getJobDataMap().getLong("userId");
            String message = context.getJobDetail().getJobDataMap().getString("message");

            // Fetch the user
            UserEntity user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Create a new notification
            NotificationEntity notification = new NotificationEntity();
            notification.setUser(user);
            notification.setMessage(message);
            notification.setTriggerTime(LocalDateTime.now());
            notification.setRead(false);

            // Save the notification
            notificationRepository.save(notification);

            System.out.println("Notification created for user: " + user.getEmail());
        } catch (Exception e) {
            throw new JobExecutionException("Failed to execute notification job", e);
        }
    }
}