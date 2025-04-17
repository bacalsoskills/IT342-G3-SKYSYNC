package edu.cit.Skysync.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Fetch weather using city name
    @GetMapping("/weeklyByCity")
    public List<DailyWeatherDTO> getWeeklyWeatherByCity(@RequestParam String city) {
        return weatherService.getWeeklyWeatherByCity(city);
    }

    @GetMapping("/todayByCity")
    public DailyWeatherDTO getTodaysWeatherByCity(@RequestParam String city) {
        JSONObject cityLocationData = weatherService.getLocationData(city);
        if (cityLocationData == null) {
            throw new RuntimeException("City not found");
        }

        double latitude = (double) cityLocationData.get("latitude");
        double longitude = (double) cityLocationData.get("longitude");
        return weatherService.getTodaysWeather(latitude, longitude);
    }
}
