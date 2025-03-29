package edu.cit.Skysync.dto;

import org.json.simple.JSONArray;

public class WeatherResponseDTO {
    private JSONArray dates;
    private JSONArray maxTemperatures;
    private JSONArray minTemperatures;

    public WeatherResponseDTO(JSONArray dates, JSONArray maxTemperatures, JSONArray minTemperatures) {
        this.dates = dates;
        this.maxTemperatures = maxTemperatures;
        this.minTemperatures = minTemperatures;
    }

    public JSONArray getDates() {
        return dates;
    }

    public JSONArray getMaxTemperatures() {
        return maxTemperatures;
    }

    public JSONArray getMinTemperatures() {
        return minTemperatures;
    }
}
