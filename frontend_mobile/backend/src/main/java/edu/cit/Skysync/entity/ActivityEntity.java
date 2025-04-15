package edu.cit.Skysync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JoinColumn(name = "user_id")
    private UserEntity user;
    

    public ActivityEntity(String name, String description, String weatherCondition) {
        this.name = name;
        this.description = description;
        this.weatherCondition = weatherCondition;
    }
}