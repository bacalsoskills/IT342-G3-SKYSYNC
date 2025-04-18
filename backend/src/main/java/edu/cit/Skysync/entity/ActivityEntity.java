package edu.cit.Skysync.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_entity")  // Explicit table name
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")  // Changed column name
    private Long activityId;  // Renamed field
    
    private String name;
    private String description;
    private String weatherCondition;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntity> schedules;

    public ActivityEntity(String name, String description, String weatherCondition) {
        this.name = name;
        this.description = description;
        this.weatherCondition = weatherCondition;
    }

    // Getters
    public Long getActivityId() {
        return activityId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public UserEntity getUser() {
        return user;
    }

    // Setters
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}