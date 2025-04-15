package edu.cit.Skysync.dto;

public class DailyWeatherDTO {
    private String date;
    private double minTemp;
    private double maxTemp;
    private int weatherCode;
    private String weatherDescription;
    private String dayOfWeek;

    public DailyWeatherDTO(String date, double minTemp, double maxTemp, 
                          int weatherCode, String weatherDescription, String dayOfWeek) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherCode = weatherCode;
        this.weatherDescription = weatherDescription;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and setters for all fields
    public String getDate() {
        return date;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}