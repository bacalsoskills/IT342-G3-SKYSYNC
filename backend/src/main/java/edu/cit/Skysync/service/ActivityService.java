package edu.cit.Skysync.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.RecommendedActivityEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.ActivityRepository;
import edu.cit.Skysync.repository.RecommendedActivityRepository;
import edu.cit.Skysync.repository.ScheduleRepository; // Import ScheduleRepository

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final RecommendedActivityRepository recommendedActivityRepository;
    private final ScheduleRepository scheduleRepository;

    public ActivityService(ActivityRepository activityRepository, 
                           RecommendedActivityRepository recommendedActivityRepository,
                           ScheduleRepository scheduleRepository) {
        this.activityRepository = activityRepository;
        this.recommendedActivityRepository = recommendedActivityRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<RecommendedActivityEntity> getRecommendedActivities(String weatherCondition) {
        // Log the weather condition for debugging
        System.out.println("Fetching activities for weather condition: " + weatherCondition);

        // Fetch recommendations from the database based on the weather condition
        return recommendedActivityRepository.findByWeatherConditionIgnoreCase(weatherCondition);
    }

    public String getWeatherDescription(int weatherCode) {
        // Map weather codes to descriptions
        switch (weatherCode) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: return "Fog";
            case 48: return "Fog";
            case 51: return "Rainy";
            case 53: return "Rainy";
            case 55: return "Rainy";
            case 61: return "Rainy";
            case 63: return "Rainy";
            case 65: return "Rainy";
            case 71: return "Snowy";
            case 73: return "Snowy";
            case 75: return "Snowy";
            case 80: return "Rainy";
            case 81: return "Rainy";
            case 82: return "Rainy";
            default: return "Any";
        }
    }

    public ActivityEntity saveUserActivity(ActivityEntity activity, UserEntity user) {
        activity.setUser(user);
        return activityRepository.save(activity);
    }

    public List<ActivityEntity> getUserActivities(UserEntity user) {
        return activityRepository.findByUser(user);
    }

   /*  public List<ActivityEntity> getRecommendedActivitiesByDescription(String weatherDescription) {
        return recommendedActivityRepository.findByWeatherCondition(weatherDescription).stream()
                .map(recommendedActivity -> new ActivityEntity(recommendedActivity.getName(), recommendedActivity.getDescription(), recommendedActivity.getWeatherCondition()))
                .collect(Collectors.toList());
    }*/

    @Transactional // Ensure this method runs within a transaction
    public boolean deleteActivityById(Long activityId) {
        if (activityRepository.existsById(activityId)) {
            // Delete associated schedules first
            scheduleRepository.deleteByActivity_ActivityId(activityId);

            // Then delete the activity
            activityRepository.deleteById(activityId);
            return true;
        }
        return false;
    }
}