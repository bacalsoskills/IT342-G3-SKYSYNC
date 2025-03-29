package edu.cit.Skysync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.WeatherResponseDTO;
import edu.cit.Skysync.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Fetch weather using latitude & longitude
    @GetMapping("/weekly")
    public WeatherResponseDTO getWeeklyWeather(@RequestParam double latitude, @RequestParam double longitude) {
        return weatherService.getWeeklyWeather(latitude, longitude);
    }

    // Fetch weather using city name
    @GetMapping("/weeklyByCity")
    public WeatherResponseDTO getWeeklyWeatherByCity(@RequestParam String city) {
        return weatherService.getWeeklyWeatherByCity(city);
    }
}
