package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.service.WardrobeService;
import edu.cit.Skysync.service.WardrobeService.WardrobeRecommendation;
import edu.cit.Skysync.service.WeatherService;

@RestController
@RequestMapping("/wardrobe")
public class WardrobeController {
    private final WardrobeService wardrobeService;
    private final WeatherService weatherService;

    public WardrobeController(WardrobeService wardrobeService, WeatherService weatherService) {
        this.wardrobeService = wardrobeService;
        this.weatherService = weatherService;
    }

    // Get recommendation by weather code
    @GetMapping("/byCode")
    public List<WardrobeRecommendation> getRecommendationByCode(@RequestParam int weatherCode) {
        return wardrobeService.getOutfitRecommendation(weatherCode);
    }

    // Get recommendation for today by location
    @GetMapping("/today")
    public List<WardrobeRecommendation> getTodayRecommendation(@RequestParam double latitude, @RequestParam double longitude) {
        List<DailyWeatherDTO> weather = weatherService.getWeeklyWeather(latitude, longitude);
        if (!weather.isEmpty()) {
            return wardrobeService.getTodayOutfitRecommendation(weather.get(0));
        }
        return wardrobeService.getOutfitRecommendation(-1); // Default recommendation
    }

    // Get recommendation for today by city
    @GetMapping("/todayByCity")
    public List<WardrobeRecommendation> getTodayRecommendationByCity(@RequestParam String city) {
        List<DailyWeatherDTO> weather = weatherService.getWeeklyWeatherByCity(city);
        if (weather != null && !weather.isEmpty()) {
            return wardrobeService.getTodayOutfitRecommendation(weather.get(0));
        }
        return wardrobeService.getOutfitRecommendation(-1); // Default recommendation
    }
}