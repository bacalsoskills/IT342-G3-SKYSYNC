import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, Button } from "antd"; // Import Button
import { useNavigate } from "react-router-dom"; // Import useNavigate
import { getWeeklyWeatherByCity } from "../services/weatherService";
import UserHeader from "../components/userHeader";

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
    <div className="min-vh-100 u-fixed-background">
      <UserHeader />
    <div className="container" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>Weekly Weather Forecast for {city}</h2>
      <Card style={{background:"none"}}>
        {loading ? (
          <div style={{ textAlign: "center" }}>
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
          <div className="container">
            {/* First row: 4 cards */}
            <div className="row">
              {weeklyWeather.slice(0, 4).map((day, index) => (
                <div className="col-12 col-md-6 col-lg-3 mb-4" key={index}>
                  <Card
                    style={{
                      padding: "16px",
                      borderRadius: "8px",
                      backgroundColor: "#f8f9fa",
                      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                      textAlign: "center",
                    }}
                  >
                    <h3 style={{ marginBottom: "8px" }}>{day.dayOfWeek}</h3>
                    <p style={{ fontSize: "14px", color: "#666", marginBottom: "16px" }}>
                      {day.date}
                    </p>
                    <p>
                      <strong>Min Temp:</strong> {day.minTemp}°C
                    </p>
                    <p>
                      <strong>Max Temp:</strong> {day.maxTemp}°C
                    </p>
                    <p>
                      <strong>Conditions:</strong> {day.weatherDescription}
                    </p>
                  </Card>
                </div>
              ))}
            </div>
            {/* Second row: remaining cards, centered */}
            <div className="row justify-content-center">
              {weeklyWeather.slice(4).map((day, index) => (
                <div className="col-12 col-md-6 col-lg-3 mb-4" key={index + 4}>
                  <Card
                    style={{
                      padding: "16px",
                      borderRadius: "8px",
                      backgroundColor: "#f8f9fa",
                      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
                      textAlign: "center",
                    }}
                  >
                    <h3 style={{ marginBottom: "8px" }}>{day.dayOfWeek}</h3>
                    <p style={{ fontSize: "14px", color: "#666", marginBottom: "16px" }}>
                      {day.date}
                    </p>
                    <p>
                      <strong>Min Temp:</strong> {day.minTemp}°C
                    </p>
                    <p>
                      <strong>Max Temp:</strong> {day.maxTemp}°C
                    </p>
                    <p>
                      <strong>Conditions:</strong> {day.weatherDescription}
                    </p>
                  </Card>
                </div>
              ))}
            </div>
          </div>
        ) : (
          <p>No weekly weather data available.</p>
        )}
      </Card>
    </div>
    </div>
  );
};

export default WeeklyForecast;