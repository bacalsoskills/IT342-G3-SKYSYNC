package edu.cit.Skysync.dto;

import java.util.List;

public class WardrobeRecommendationDTO {
    private String date;
    private String theme;
    private List<ClothingRecommendation> clothingRecommendations;

    public WardrobeRecommendationDTO(String date, String theme, List<ClothingRecommendation> clothingRecommendations) {
        this.date = date;
        this.theme = theme;
        this.clothingRecommendations = clothingRecommendations;
    }

    public String getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public List<ClothingRecommendation> getClothingRecommendations() {
        return clothingRecommendations;
    }

    public static class ClothingRecommendation {
        private String clothingItem;
        private String description;

        public ClothingRecommendation(String clothingItem, String description) {
            this.clothingItem = clothingItem;
            this.description = description;
        }

        public String getClothingItem() {
            return clothingItem;
        }

        public String getDescription() {
            return description;
        }
    }
}
