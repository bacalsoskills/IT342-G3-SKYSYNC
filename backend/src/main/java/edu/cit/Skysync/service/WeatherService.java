package edu.cit.Skysync.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import edu.cit.Skysync.dto.DailyWeatherDTO;

@Service
public class WeatherService {
    public List<DailyWeatherDTO> getWeeklyWeather(double latitude, double longitude) {
    try {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                "&longitude=" + longitude + "&daily=temperature_2m_max,temperature_2m_min&hourly=weather_code&timezone=auto";
        HttpURLConnection apiConnection = fetchApiResponse(url);

        if (apiConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to fetch weather data");
        }

        String jsonResponse = readApiResponse(apiConnection);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
        
        JSONObject daily = (JSONObject) jsonObject.get("daily");
        JSONObject hourly = (JSONObject) jsonObject.get("hourly");

        JSONArray dates = (JSONArray) daily.get("time");
        JSONArray maxTemps = (JSONArray) daily.get("temperature_2m_max");
        JSONArray minTemps = (JSONArray) daily.get("temperature_2m_min");
        JSONArray weatherCodes = (JSONArray) hourly.get("weather_code");
        
        List<DailyWeatherDTO> weatherList = new ArrayList<>();
        
        for (int i = 0; i < dates.size(); i++) {
            String dateStr = (String) dates.get(i);
            double maxTemp = (double) maxTemps.get(i);
            double minTemp = (double) minTemps.get(i);
        
            // Parse the date string (assuming format is YYYY-MM-DD)
            LocalDate date = LocalDate.parse(dateStr);
            String dayOfWeek = date.getDayOfWeek().toString(); // Gets enum like MONDAY, TUESDAY
            dayOfWeek = dayOfWeek.substring(0, 1) + dayOfWeek.substring(1).toLowerCase(); // Formats to "Monday"
            
            int weatherCode = getMostFrequentWeatherCodeValue(weatherCodes, i);
            String weatherDescription = getWeatherDescription(weatherCode);
        
            weatherList.add(new DailyWeatherDTO(dateStr, minTemp, maxTemp, weatherCode, weatherDescription, dayOfWeek));
        }
        
        return weatherList;
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
    

    public List<DailyWeatherDTO> getWeeklyWeatherByCity(String city) {
        try {
            JSONObject cityLocationData = getLocationData(city);
            if (cityLocationData == null) {
                throw new RuntimeException("City not found");
            }

            double latitude = (double) cityLocationData.get("latitude");
            double longitude = (double) cityLocationData.get("longitude");
            return getWeeklyWeather(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DailyWeatherDTO getTodaysWeather(double latitude, double longitude) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&daily=temperature_2m_max,temperature_2m_min&hourly=weather_code&timezone=auto";
            HttpURLConnection apiConnection = fetchApiResponse(url);

            if (apiConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch weather data");
            }

            String jsonResponse = readApiResponse(apiConnection);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

            JSONObject daily = (JSONObject) jsonObject.get("daily");
            JSONObject hourly = (JSONObject) jsonObject.get("hourly");

            JSONArray dates = (JSONArray) daily.get("time");
            JSONArray maxTemps = (JSONArray) daily.get("temperature_2m_max");
            JSONArray minTemps = (JSONArray) daily.get("temperature_2m_min");
            JSONArray weatherCodes = (JSONArray) hourly.get("weather_code");

            // Get today's date
            LocalDate today = LocalDate.now();
            String todayStr = today.toString();

            // Find today's weather data
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).equals(todayStr)) {
                    double maxTemp = (double) maxTemps.get(i);
                    double minTemp = (double) minTemps.get(i);

                    int weatherCode = getMostFrequentWeatherCodeValue(weatherCodes, i);
                    String weatherDescription = getWeatherDescription(weatherCode);

                    String dayOfWeek = today.getDayOfWeek().toString();
                    dayOfWeek = dayOfWeek.substring(0, 1) + dayOfWeek.substring(1).toLowerCase(); // Format to "Monday"

                    return new DailyWeatherDTO(todayStr, minTemp, maxTemp, weatherCode, weatherDescription, dayOfWeek);
                }
            }

            throw new RuntimeException("No weather data available for today");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getLocationData(String city) {
        city = city.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";

        try {
            HttpURLConnection apiConnection = fetchApiResponse(urlString);
            if (apiConnection.getResponseCode() != 200) {
                return null;
            }

            String jsonResponse = readApiResponse(apiConnection);
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
            return locationData.isEmpty() ? null : (JSONObject) locationData.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readApiResponse(HttpURLConnection apiConnection) throws IOException {
        StringBuilder resultJson = new StringBuilder();
        Scanner scanner = new Scanner(apiConnection.getInputStream());
        while (scanner.hasNext()) {
            resultJson.append(scanner.nextLine());
        }
        scanner.close();
        return resultJson.toString();
    }

    private HttpURLConnection fetchApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    private String getWeatherDescription(int code) {
        switch (code) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: return "Fog";
            case 48: return "Depositing rime fog";
            case 51: return "Drizzle: Light";
            case 53: return "Drizzle: Moderate";
            case 55: return "Drizzle: Dense";
            case 61: return "Rain: Slight";
            case 63: return "Rain: Moderate";
            case 65: return "Rain: Heavy";
            case 71: return "Snow fall: Slight";
            case 73: return "Snow fall: Moderate";
            case 75: return "Snow fall: Heavy";
            case 80: return "Rain showers: Slight";
            case 81: return "Rain showers: Moderate";
            case 82: return "Rain showers: Heavy";
            default: return "Unknown";
        }
    }

    private int getMostFrequentWeatherCodeValue(JSONArray weatherCodes, int dayIndex) {
        int start = dayIndex * 24;
        int end = Math.min(start + 24, weatherCodes.size());

        // Define priority weather codes (from 45 to 82)
        List<Integer> priorityWeatherCodes = List.of(
            45, 48, // Fog-related codes
            51, 53, 55, // Drizzle-related codes
            61, 63, 65, // Rain-related codes
            71, 73, 75, // Snow-related codes
            80, 81, 82  // Rain showers
        );

        // Calculate the weighted frequency of weather codes
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int i = start; i < end; i++) {
            int code = ((Long) weatherCodes.get(i)).intValue();
            int weight = priorityWeatherCodes.contains(code) ? 2 : 1; // Priority codes get +2, others get +1
            frequencyMap.put(code, frequencyMap.getOrDefault(code, 0) + weight);
        }

        // Return the most frequent weather code
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0); // Default to clear sky if no data available
    }


    public DailyWeatherDTO getTodaysWeatherByCity(String city) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTodaysWeatherByCity'");
    }
}