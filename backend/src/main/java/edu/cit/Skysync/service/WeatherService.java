package edu.cit.Skysync.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import edu.cit.Skysync.dto.WeatherResponseDTO;

@Service
public class WeatherService {
    public WeatherResponseDTO getWeeklyWeather(double latitude, double longitude) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&daily=temperature_2m_max,temperature_2m_min&timezone=auto";
            HttpURLConnection apiConnection = fetchApiResponse(url);

            if (apiConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed to fetch weather data");
            }

            String jsonResponse = readApiResponse(apiConnection);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject daily = (JSONObject) jsonObject.get("daily");

            JSONArray dates = (JSONArray) daily.get("time");
            JSONArray maxTemps = (JSONArray) daily.get("temperature_2m_max");
            JSONArray minTemps = (JSONArray) daily.get("temperature_2m_min");

            return new WeatherResponseDTO(dates, maxTemps, minTemps);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WeatherResponseDTO getWeeklyWeatherByCity(String city) {
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
                System.out.println("Geocoding API Error: " + apiConnection.getResponseCode());
                return null;
            }
    
            String jsonResponse = readApiResponse(apiConnection);
            System.out.println("Geocoding API Response: " + jsonResponse);  // Debugging line
    
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);
            JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
    
            if (locationData.isEmpty()) {
                System.out.println("No location data found for city: " + city);
                return null;
            }
    
            return (JSONObject) locationData.get(0);
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
}
