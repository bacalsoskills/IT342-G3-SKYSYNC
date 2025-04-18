package edu.cit.Skysync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommended_activities")
public class RecommendedActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendedActivityId;

    private String name;
    private String description;
    private String weatherCondition;

    // Getters and Setters
    public Long getRecommendedActivityId() {
        return recommendedActivityId;
    }

    public void setRecommendedActivityId(Long recommendedActivityId) {
        this.recommendedActivityId = recommendedActivityId;
    }

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
