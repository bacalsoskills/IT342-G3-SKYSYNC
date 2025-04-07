package edu.cit.Skysync.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.entity.UserEntity;
import edu.cit.Skysync.repository.ActivityRepository;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }
    
    public List<ActivityEntity> getRecommendedActivities(int weatherCode) {
        String weatherCondition = getWeatherDescription(weatherCode);
        return getActivitiesForWeather(weatherCondition);
    }
    
    private List<ActivityEntity> getActivitiesForWeather(String weatherCondition) {
        return switch (weatherCondition) {
            case "Clear sky", "Mainly clear" -> List.of(
                new ActivityEntity("Beach Day", "Perfect for sunny weather", "Clear sky"),
                new ActivityEntity("Picnic", "Enjoy outdoor dining", "Clear sky"),
                new ActivityEntity("Hiking", "Explore nature trails", "Clear sky"),
                new ActivityEntity("Outdoor Sports", "Basketball, tennis, or soccer", "Clear sky")
            );
            case "Partly cloudy" -> List.of(
                new ActivityEntity("City Walk", "Comfortable walking in partly cloudy weather", "Partly cloudy"),
                new ActivityEntity("Outdoor Photography", "Great lighting conditions", "Partly cloudy"),
                new ActivityEntity("Gardening", "Perfect weather for working outside", "Partly cloudy")
            );
            case "Overcast" -> List.of(
                new ActivityEntity("Museum Visit", "Ideal for overcast days", "Overcast"),
                new ActivityEntity("Coffee Shop Reading", "Cozy indoor activity", "Overcast"),
                new ActivityEntity("Indoor Rock Climbing", "Stay active despite the clouds", "Overcast")
            );
            case "Fog", "Depositing rime fog" -> List.of(
                new ActivityEntity("Fog Photography", "Unique atmospheric conditions", "Fog"),
                new ActivityEntity("Indoor Yoga", "Stay active despite the fog", "Fog"),
                new ActivityEntity("Visit a Greenhouse", "Enjoy plants in a misty environment", "Fog")
            );
            case "Drizzle: Light", "Drizzle: Moderate", "Drizzle: Dense", 
                 "Rain: Slight", "Rain: Moderate", "Rain: Heavy",
                 "Rain showers: Slight", "Rain showers: Moderate", "Rain showers: Heavy" -> List.of(
                new ActivityEntity("Movie Marathon", "Perfect for rainy days", "Rainy"),
                new ActivityEntity("Baking", "Warm up the house", "Rainy"),
                new ActivityEntity("Visit a Library", "Quiet time with books", "Rainy"),
                new ActivityEntity("Board Game Cafe", "Social indoor activity", "Rainy")
            );
            case "Snow fall: Slight", "Snow fall: Moderate", "Snow fall: Heavy" -> List.of(
                new ActivityEntity("Skiing/Snowboarding", "Enjoy the snow", "Snowy"),
                new ActivityEntity("Hot Chocolate by the Fire", "Cozy winter activity", "Snowy"),
                new ActivityEntity("Build a Snowman", "Classic winter fun", "Snowy"),
                new ActivityEntity("Winter Photography", "Capture beautiful snowy scenes", "Snowy")
            );
            default -> List.of(
                new ActivityEntity("Board Games", "Fun for any weather", "Any"),
                new ActivityEntity("Reading", "Always a good choice", "Any"),
                new ActivityEntity("DIY Projects", "Creative indoor time", "Any")
            );
        };
    }
    
    private String getWeatherDescription(int code) {
        switch (code) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: return "Fog";
            case 48: return "Depositing rime fog";
            case 51: return "Drizzle: Light";
            case 53: return "Drizzle: Moderate";
            case 55: return "Drizzle: Dense";
            case 61: return "Rain: Slight";
            case 63: return "Rain: Moderate";
            case 65: return "Rain: Heavy";
            case 71: return "Snow fall: Slight";
            case 73: return "Snow fall: Moderate";
            case 75: return "Snow fall: Heavy";
            case 80: return "Rain showers: Slight";
            case 81: return "Rain showers: Moderate";
            case 82: return "Rain showers: Heavy";
            default: return "Unknown";
        }
    }
    
    public ActivityEntity saveUserActivity(ActivityEntity activity, UserEntity user) {
        activity.setUser(user);
        return activityRepository.save(activity);
    }
    
    public List<ActivityEntity> getUserActivities(UserEntity user) {
        return activityRepository.findByUser(user);
    }
    
    public List<ActivityEntity> getRecommendedActivitiesByDescription(String weatherDescription) {
        return getActivitiesForWeather(weatherDescription);
    }

    public boolean deleteActivityById(Long activityId) {
        if (activityRepository.existsById(activityId)) {
            activityRepository.deleteById(activityId);
            return true;
        }
        return false;
    }
}