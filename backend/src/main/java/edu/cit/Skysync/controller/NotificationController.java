package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.repository.NotificationRepository;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/user/{userId}")
    public List<NotificationEntity> getUserNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }
}