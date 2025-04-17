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
        // Fetch recommendations from the database based on the weather code
        List<WardrobeRecommendationEntity> entities = wardrobeRecommendationRepository.findByWeatherCode(weatherCode);
        return entities.stream()
                .map(entity -> new WardrobeRecommendation(
                        entity.getTheme(),
                        parseJsonArray(entity.getClothingItems()),
                        parseJsonArray(entity.getClothingDescriptions())
                ))
                .collect(Collectors.toList());
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