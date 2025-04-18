package edu.cit.Skysync.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.entity.WardrobeRecommendationEntity;
import edu.cit.Skysync.repository.WardrobeRecommendationRepository;

@Service
public class WardrobeService {

    private final WardrobeRecommendationRepository wardrobeRecommendationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON parsing

    public WardrobeService(WardrobeRecommendationRepository wardrobeRecommendationRepository) {
        this.wardrobeRecommendationRepository = wardrobeRecommendationRepository;
    }

    public List<WardrobeRecommendation> getOutfitRecommendation(int weatherCode) {
        // Map weather codes to their corresponding groups
        int normalizedWeatherCode = normalizeWeatherCode(weatherCode);

        // Fetch recommendations from the database based on the normalized weather code
        List<WardrobeRecommendationEntity> entities = wardrobeRecommendationRepository.findByWeatherCode(normalizedWeatherCode);
        return entities.stream()
                .map(entity -> new WardrobeRecommendation(
                        entity.getTheme(),
                        parseJsonArray(entity.getClothingItems()),
                        parseJsonArray(entity.getClothingDescriptions())
                ))
                .collect(Collectors.toList());
    }

    // Helper method to normalize weather codes
    private int normalizeWeatherCode(int weatherCode) {
        // Define groups of weather codes
        if (weatherCode == 71 || weatherCode == 73 || weatherCode == 75) {
            return 71; // Group snowy weather codes under 71
        } else if (weatherCode == 51 || weatherCode == 53 || weatherCode == 55 || weatherCode == 61 || weatherCode == 63 || weatherCode == 65 || weatherCode == 80 || weatherCode == 81 || weatherCode == 82) {
            return 51; // Group rainy weather codes under 51
        } else if (weatherCode == 45 || weatherCode == 48) {
            return 45; // Group foggy weather codes under 45
        } else if (weatherCode == 0 || weatherCode == 1) {
            return 0; // Group clear weather codes under 0
        } else if (weatherCode == 2) {
            return 2; // Partly cloudy
        } else if (weatherCode == 3) {
            return 3; // Overcast
        } else {
            return -1; // Default recommendation
        }
    }

    public List<WardrobeRecommendation> getTodayOutfitRecommendation(DailyWeatherDTO weather) {
        // Use the weather code from today's weather to fetch recommendations
        return getOutfitRecommendation(weather.getWeatherCode());
    }

    private String[] parseJsonArray(String jsonArray) {
        try {
            // Parse the JSON string into a List<String> and convert it to an array
            return objectMapper.readValue(jsonArray, new TypeReference<List<String>>() {}).toArray(new String[0]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON array: " + jsonArray, e);
        }
    }

    public static class WardrobeRecommendation {
        private String theme;
        private String[] clothingItems;
        private String[] clothingDescriptions;

        public WardrobeRecommendation(String theme, String[] clothingItems, String[] clothingDescriptions) {
            this.theme = theme;
            this.clothingItems = clothingItems;
            this.clothingDescriptions = clothingDescriptions;
        }

        // Getters
        public String getTheme() {
            return theme;
        }

        public String[] getClothingItems() {
            return clothingItems;
        }

        public String[] getClothingDescriptions() {
            return clothingDescriptions;
        }
    }
}