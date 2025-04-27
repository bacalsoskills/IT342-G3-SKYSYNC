package edu.cit.Skysync.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.ActivityDTO;
import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.RecommendedActivityEntity;
import edu.cit.Skysync.service.ActivityService;
import edu.cit.Skysync.service.UserService;
import edu.cit.Skysync.service.WeatherService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;
    private final WeatherService weatherService;

    public ActivityController(ActivityService activityService, UserService userService, WeatherService weatherService) {
        this.activityService = activityService;
        this.userService = userService;
        this.weatherService = weatherService;
    }

    // Save an activity for a user
    @PostMapping
    public ResponseEntity<ActivityEntity> saveActivity(@RequestParam Long userId, 
                                                        @RequestBody ActivityEntity activity) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(activityService.saveUserActivity(activity, user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get activities for a user
    @GetMapping("/user")
    public ResponseEntity<List<ActivityEntity>> getUserActivities(@RequestParam Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(activityService.getUserActivities(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Get activity recommendations by city
    @GetMapping("/recommendationsByCity")
    public ResponseEntity<List<ActivityDTO>> getRecommendationsByCity(@RequestParam String city) {
        var weather = weatherService.getWeeklyWeatherByCity(city);
        if (weather != null && !weather.isEmpty()) {
            int weatherCode = weather.get(0).getWeatherCode(); // Get today's weather code
            String weatherCondition = activityService.getWeatherDescription(weatherCode);

            // Fetch recommendations from the database
            List<RecommendedActivityEntity> recommendations = activityService.getRecommendedActivities(weatherCondition);

            // Map RecommendedActivityEntity to ActivityDTO
            List<ActivityDTO> activityDTOs = recommendations.stream()
                .map(rec -> new ActivityDTO(
                    rec.getName(),
                    rec.getDescription(),
                    rec.getWeatherCondition()
                ))
                .collect(Collectors.toList());

            return ResponseEntity.ok(activityDTOs);
        }
        return ResponseEntity.notFound().build(); // Return 404 if no weather data is available
    }

    // Delete an activity by ID
    @DeleteMapping
    public ResponseEntity<String> deleteActivity(@RequestParam Long activityId) {
        boolean isDeleted = activityService.deleteActivityById(activityId);
        if (isDeleted) {
            return ResponseEntity.ok("Activity and associated schedules deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}