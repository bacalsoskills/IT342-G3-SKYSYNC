package edu.cit.Skysync.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
            JSONObject hourly = (JSONObject) jsonObject.get("hourly"); // ✅ Get weather_code from "hourly"
    
            JSONArray dates = (JSONArray) daily.get("time");
            JSONArray maxTemps = (JSONArray) daily.get("temperature_2m_max");
            JSONArray minTemps = (JSONArray) daily.get("temperature_2m_min");
            JSONArray weatherCodes = (JSONArray) hourly.get("weather_code"); // ✅ Get weather codes
            
            List<DailyWeatherDTO> weatherList = new ArrayList<>();
            
            for (int i = 0; i < dates.size(); i++) {
                String date = (String) dates.get(i);
                double maxTemp = (double) maxTemps.get(i);
                double minTemp = (double) minTemps.get(i);
            
                // ✅ Get the most frequent weather code for the day
                int weatherCode = getMostFrequentWeatherCodeValue(weatherCodes, i);
                String weatherDescription = getWeatherDescription(weatherCode);
            
                // ✅ Now we pass both the weather code (int) and description (String)
                weatherList.add(new DailyWeatherDTO(date, minTemp, maxTemp, weatherCode, weatherDescription));
            }
            
    
            return weatherList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // ✅ Return empty list instead of null
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

    private JSONObject getLocationData(String city) {
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
    
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int i = start; i < end; i++) {
            int code = ((Long) weatherCodes.get(i)).intValue();
            frequencyMap.put(code, frequencyMap.getOrDefault(code, 0) + 1);
        }
    
        return frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0); // Default to clear sky if no data available
    }
    

}