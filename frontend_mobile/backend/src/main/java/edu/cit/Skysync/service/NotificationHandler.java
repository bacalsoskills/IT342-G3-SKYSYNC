package edu.cit.Skysync.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.event.ScheduleCreatedEvent;
import edu.cit.Skysync.repository.NotificationRepository;

@Component
public class NotificationHandler {
    private final NotificationRepository notificationRepo;

    public NotificationHandler(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @EventListener
    @Transactional
    public void handleScheduleCreated(ScheduleCreatedEvent event) {
        // Create new notification
        NotificationEntity notification = new NotificationEntity();
        
        // Set all required fields
        notification.setUser(event.getSchedule().getUser());
        notification.setTriggerTime(event.getSchedule().getStartTime());
        notification.setMessage(
            "New schedule: " + event.getSchedule().getActivity().getName() + 
            " at " + event.getSchedule().getStartTime().toLocalTime()
        );
        
        // Set the schedule
        notification.setSchedule(event.getSchedule());
        
        // Set default read status
        notification.setRead(false);
        
        // Save to database
        notificationRepo.save(notification);
        
        // Optional debug logging
        System.out.println("Created notification for schedule: " + 
                         event.getSchedule().getScheduleId());
    }
}