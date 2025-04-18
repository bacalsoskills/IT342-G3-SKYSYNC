package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.service.WardrobeService;
import edu.cit.Skysync.service.WardrobeService.WardrobeRecommendation;
import edu.cit.Skysync.service.WeatherService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/wardrobe")
public class WardrobeController {

    private final WardrobeService wardrobeService;
    private final WeatherService weatherService;

    public WardrobeController(WardrobeService wardrobeService, WeatherService weatherService) {
        this.wardrobeService = wardrobeService;
        this.weatherService = weatherService;
    }

    // Get recommendation for today by city
    @GetMapping("/todayByCity")
    public List<WardrobeRecommendation> getTodayRecommendationByCity(@RequestParam String city) {
        List<DailyWeatherDTO> weather = weatherService.getWeeklyWeatherByCity(city);
        if (weather != null && !weather.isEmpty()) {
            // Use the first day's weather (today's weather)
            return wardrobeService.getTodayOutfitRecommendation(weather.get(0));
        }
        return wardrobeService.getOutfitRecommendation(-1); // Default recommendation
    }
}