package edu.cit.Skysync.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.cit.Skysync.entity.NotificationEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.NotificationRepository;
import jakarta.transaction.Transactional;

@Service
public class DailyWeatherNotificationService {

    private final UserService userService;
    private final WeatherService weatherService;
    private final NotificationRepository notificationRepository;

    public DailyWeatherNotificationService(UserService userService, WeatherService weatherService, NotificationRepository notificationRepository) {
        this.userService = userService;
        this.weatherService = weatherService;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 8 * * ?") // Runs every day at 8:00 AM
    public void sendDailyWeatherNotifications() {
        List<UserEntity> users = userService.getAllUsers();

        // Set a consistent trigger time for all notifications
        LocalDateTime triggerTime = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0);

        // Fetch weather data for Cebu
        String city = "Cebu";
        var weatherList = weatherService.getWeeklyWeatherByCity(city);

        if (weatherList != null && !weatherList.isEmpty()) {
            String weatherDescription = weatherList.get(0).getWeatherDescription();

            for (UserEntity user : users) {
                // Create a notification
                NotificationEntity notification = new NotificationEntity();
                notification.setUser(user);
                notification.setMessage("Today's weather in " + city + ": " + weatherDescription);
                notification.setTriggerTime(triggerTime); // Use the consistent trigger time
                notification.setRead(false);

                // Save the notification
                notificationRepository.save(notification);
            }
        }
    }
}