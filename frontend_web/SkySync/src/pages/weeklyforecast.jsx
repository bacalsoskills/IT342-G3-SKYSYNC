import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, Button } from "antd"; // Import Button
import { useNavigate } from "react-router-dom"; // Import useNavigate
import { getWeeklyWeatherByCity } from "../services/weatherService";

const WeeklyForecast = () => {
  const [city, setCity] = useState(() => {
    return localStorage.getItem("lastCity") || "Cebu";
  });
  const [weeklyWeather, setWeeklyWeather] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Initialize navigate

  useEffect(() => {
    const fetchWeeklyWeather = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await getWeeklyWeatherByCity(city);
        setWeeklyWeather(data);
      } catch (err) {
        console.error("Error fetching weekly weather:", err);
        setError("Failed to fetch weekly weather. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchWeeklyWeather();
  }, [city]);

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>Weekly Weather Forecast for {city}</h2>
      <Card
        style={{
          padding: "20px",
          borderRadius: "8px",
        }}
      >
        {loading ? (
          <div style={{ textAlign: "center", padding: "20px" }}>
            <Spin size="large" />
          </div>
        ) : error ? (
          <Alert
            message={error}
            type="error"
            showIcon
            style={{ marginBottom: "16px" }}
          />
        ) : weeklyWeather ? (
          <div>
            {weeklyWeather.map((day, index) => (
              <div key={index} style={{ marginBottom: "16px" }}>
                <h3>
                  {day.date} - {day.dayOfWeek}
                </h3>
                <p>
                  <strong>Min Temp:</strong> {day.minTemp}°C
                </p>
                <p>
                  <strong>Max Temp:</strong> {day.maxTemp}°C
                </p>
                <p>
                  <strong>Conditions:</strong> {day.weatherDescription}
                </p>
              </div>
            ))}
          </div>
        ) : (
          <p>No weekly weather data available.</p>
        )}
      </Card>
    </div>
  );
};

export default WeeklyForecast;