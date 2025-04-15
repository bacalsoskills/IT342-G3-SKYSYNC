package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.service.DailyWeatherNotificationService;
import edu.cit.Skysync.service.WeatherService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notificationRepository;
    private final DailyWeatherNotificationService dailyWeatherNotificationService;
    private final WeatherService weatherService;

    public NotificationController(NotificationRepository notificationRepository, DailyWeatherNotificationService dailyWeatherNotificationService, WeatherService weatherService) {
        this.notificationRepository = notificationRepository;
        this.dailyWeatherNotificationService = dailyWeatherNotificationService;
        this.weatherService = weatherService;
    }

    @GetMapping("/user/{userId}")
    public List<NotificationEntity> getUserNotifications(@PathVariable Long userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    @GetMapping("/trigger")
    public ResponseEntity<String> triggerDailyWeatherNotifications() {
        // Trigger the daily weather notifications
        dailyWeatherNotificationService.sendDailyWeatherNotifications();

        // Fetch today's weather description for Cebu
        String city = "Cebu";
        var weatherList = weatherService.getWeeklyWeatherByCity(city);

        if (weatherList != null && !weatherList.isEmpty()) {
            String weatherDescription = weatherList.get(0).getWeatherDescription();
            return ResponseEntity.ok("Today's weather in " + city + ": " + weatherDescription);
        }

        return ResponseEntity.ok("No weather data available for " + city + " today.");
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
}