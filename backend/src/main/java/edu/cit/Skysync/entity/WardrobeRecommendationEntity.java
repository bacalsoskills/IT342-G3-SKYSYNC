package edu.cit.Skysync.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wardrobe_recommendations")
public class WardrobeRecommendationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int weatherCode;

    private String theme;

    // Store clothing items as a single JSON or comma-separated string
    private String clothingItems;

    // Store clothing descriptions as a single JSON or comma-separated string
    private String clothingDescriptions;

    // Constructors
    public WardrobeRecommendationEntity() {}

    public WardrobeRecommendationEntity(int weatherCode, String theme, String clothingItems, String clothingDescriptions) {
        this.weatherCode = weatherCode;
        this.theme = theme;
        this.clothingItems = clothingItems;
        this.clothingDescriptions = clothingDescriptions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getClothingItems() {
        return clothingItems;
    }

    public void setClothingItems(String clothingItems) {
        this.clothingItems = clothingItems;
    }

    public String getClothingDescriptions() {
        return clothingDescriptions;
    }

    public void setClothingDescriptions(String clothingDescriptions) {
        this.clothingDescriptions = clothingDescriptions;
    }
}
