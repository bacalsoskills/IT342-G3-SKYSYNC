package edu.cit.Skysync.dto;

public class DailyWeatherDTO {
    private String date;
    private double minTemp;
    private double maxTemp;
    private int weatherCode; // ✅ Store the weather code as an integer
    private String weatherDescription; // ✅ Store weather description as a string

    public DailyWeatherDTO(String date, double minTemp, double maxTemp, int weatherCode, String weatherDescription) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weatherCode = weatherCode;
        this.weatherDescription = weatherDescription;
    }

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
}
