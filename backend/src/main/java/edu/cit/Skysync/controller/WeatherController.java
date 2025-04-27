package edu.cit.Skysync.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import edu.cit.Skysync.dto.DailyWeatherDTO;
import edu.cit.Skysync.service.WeatherService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/weather")
public class WeatherController {
    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
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
        logger.info("Fetching today's weather for city: {}", city);
        JSONObject cityLocationData = weatherService.getLocationData(city);
        if (cityLocationData == null) {
            logger.error("City not found: {}", city);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found");
        }

        double latitude = (double) cityLocationData.get("latitude");
        double longitude = (double) cityLocationData.get("longitude");
        logger.info("City found: {}, Latitude: {}, Longitude: {}", city, latitude, longitude);

        try {
            DailyWeatherDTO weather = weatherService.getTodaysWeather(latitude, longitude);
            logger.info("Weather data fetched successfully: {}", weather);
            return weather;
        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {}", city, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching weather data");
        }
    }
}
