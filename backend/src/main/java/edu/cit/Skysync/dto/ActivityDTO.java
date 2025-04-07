package edu.cit.Skysync.dto;

public class ActivityDTO {
    private String name;
    private String description;
    private String weatherCondition;

    public ActivityDTO(String name, String description, String weatherCondition) {
        this.name = name;
        this.description = description;
        this.weatherCondition = weatherCondition;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
}