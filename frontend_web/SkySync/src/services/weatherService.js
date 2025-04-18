import axios from "axios";

const API_URL = "http://localhost:8080/weather";

export const getTodaysWeatherByCity = async (city) => {
  try {
    const response = await axios.get(`${API_URL}/todayByCity`, {
      params: { city },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching today's weather by city:", error);
    throw error;
  }
};

export const getWeeklyWeatherByCity = async (city) => {
  try {
    const response = await axios.get(`${API_URL}/weeklyByCity`, {
      params: { city },
    });
    return response.data; // Return the weekly weather data
  } catch (error) {
    console.error("Error fetching weekly weather by city:", error);
    throw error;
  }
};