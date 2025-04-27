package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.service.WeatherService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;
    
    private final WeatherService weatherService;

    public NotificationController(NotificationRepository notificationRepository,  WeatherService weatherService) {
        this.notificationRepository = notificationRepository;
        this.weatherService = weatherService;
    }

    @GetMapping("/user/{userId}")
    public List<NotificationEntity> getUserNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationEntity> getUnreadNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUser_IdAndIsReadFalse(userId);
    }


    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllNotifications() {
        notificationRepository.deleteAll();
        return ResponseEntity.ok("All notifications have been cleared.");
    }

    // New endpoint: Delete a notification by ID
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable Long notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
            return ResponseEntity.ok("Notification deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        return notificationRepository.findById(notificationId)
            .map(notification -> {
                notification.setRead(true); // Set isRead to true
                notificationRepository.save(notification); // Save the updated notification
                return ResponseEntity.ok("Notification marked as read.");
            })
            .orElse(ResponseEntity.notFound().build());
    }
}