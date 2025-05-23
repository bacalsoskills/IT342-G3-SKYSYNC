import React, { useState, useEffect } from "react";
import { Card, Input, Spin, Alert, List, Tag, Button } from "antd";
import { getUserDetails } from "../services/userService";
import { logout } from "../services/authService";
import { getTodaysWeatherByCity } from "../services/weatherService";
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";
import { getActivityRecommendationsByCity } from "../services/activityService";
import dayjs from "dayjs";
import weekday from "dayjs/plugin/weekday";
import { useNavigate } from "react-router-dom";
import UserHeader from "../components/userHeader";
import TimeCard from "../components/TimeCard";

dayjs.extend(weekday);

const Dashboard = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
  });
  const [loading, setLoading] = useState(true);
  const [weatherData, setWeatherData] = useState(null);
  const [wardrobeData, setWardrobeData] = useState(null);
  const [activityData, setActivityData] = useState([]);
  const [city, setCity] = useState(() => localStorage.getItem("lastCity") || "Cebu");
  const [weatherLoading, setWeatherLoading] = useState(false);
  const [wardrobeLoading, setWardrobeLoading] = useState(false);
  const [activityLoading, setActivityLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const authToken = localStorage.getItem("authToken");

    if (!authToken || !userId) {
      alert("You are not logged in. Redirecting to login...");
      window.location.href = "/login";
    } else {
      fetchUserDetails(userId);
    }

    fetchWeatherData(city);
    fetchActivityData(city);
  }, [userId]);

  const fetchUserDetails = async (userId) => {
    try {
      const authToken = localStorage.getItem("authToken");
      const userDetails = await getUserDetails(userId, authToken);
      setUser({
        firstName: userDetails.firstName,
        lastName: userDetails.lastName,
      });
    } catch (error) {
      console.error("Failed to fetch user details:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchWeatherData = async (cityName) => {
    if (!cityName.trim()) {
      setError("Please enter a city name");
      return;
    }

    setWeatherLoading(true);
    setError(null);

    try {
      const data = await getTodaysWeatherByCity(cityName);
      if (data) {
        setWeatherData(data);
        setError(null); // Clear error if weather data is successfully fetched
        await fetchWardrobeData(cityName); // Fetch wardrobe data based on the weather
      } else {
        setWeatherData(null); // Clear previous weather data
        setError("City not found. Please check the spelling or try another city."); // Display error for incorrect city
      }
    } catch (err) {
      console.error("Error fetching weather data:", err);
      setWeatherData(null); // Clear previous weather data
      setError("City not found. Please check the spelling or try another city."); // Handle incorrect city
    } finally {
      setWeatherLoading(false);
    }
  };



  const fetchWardrobeData = async (cityName) => {
    setWardrobeLoading(true);
    try {
      const data = await getWardrobeRecommendationByCity(cityName);
      setWardrobeData(data);
    } catch (err) {
      console.error("Error fetching wardrobe recommendations:", err);
      setError("Failed to fetch wardrobe recommendations");
    } finally {
      setWardrobeLoading(false);
    }
  };

  const fetchActivityData = async (cityName) => {
    setActivityLoading(true);
    try {
      const data = await getActivityRecommendationsByCity(cityName);
      setActivityData(data);
    } catch (err) {
      console.error("Error fetching activity recommendations:", err);
      setError("Failed to fetch activity recommendations");
    } finally {
      setActivityLoading(false);
    }
  };

  const handleLogout = async () => {
    const authToken = localStorage.getItem("authToken");
    if (!authToken) {
      alert("No token found. Redirecting to home...");
      window.location.href = "/";
      return;
    }
    try {
      await logout(authToken);
      localStorage.removeItem("authToken");
      localStorage.removeItem("userId");
      alert("You have been logged out.");
      window.location.href = "/";
    } catch (error) {
      console.error("Failed to log out:", error);
      alert("Failed to log out. Please try again.");
    }
  };

  const handleProfile = () => {
    navigate("/userprofile");
  };

  const handleCityChange = (newCity) => {
    setCity(newCity);
    localStorage.setItem("lastCity", newCity);
  };

  const getDayOfWeek = (dateString) => {
    return dayjs(dateString).format("dddd");
  };

  return (
    <div className="min-vh-100">
      <UserHeader />
      <div className="u-fixed-background" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
        {/* Main Content */}
        <div className="container mt-4">
          <div className="row">
            {/* Weather Forecast Section */}
            <div className="col-12 mb-4">
              <h2>Weather Forecast</h2>
              <div style={{ marginBottom: "16px" }}>
                <Input.Search
                  placeholder="Enter city name"
                  enterButton="Search"
                  size="large"
                  value={city}
                  onChange={(e) => handleCityChange(e.target.value)}
                  onSearch={() => {
                    fetchWeatherData(city);
                    fetchActivityData(city);
                  }}
                  loading={weatherLoading}
                />
              </div>
              {/* Use Bootstrap row/col for responsive layout */}
              <div className="row">
                {/* Left Column */}
                <div className="col-12 col-md-6 mb-4">
                  <div className="d-flex flex-column gap-3">
                    <TimeCard city={city} />
                    {weatherData && (
                      <Card
                        className="d-flex flex-column align-items-center justify-content-center text-center text-white shadow rounded-3"
                        style={{
                          height: "190px",
                          backgroundColor: "#1E3A8A",
                          fontSize: "36px",
                          fontWeight: "bold",
                        }}
                      >
                        <h3 className="fw-bold m-0 p-0 fs-4">TEMPERATURE</h3>
                        <span className="fs-4 fw-normal">
                          {weatherData.minTemp}°C - {weatherData.maxTemp}°C
                        </span>
                      </Card>
                    )}
                  </div>
                </div>
                {/* Right Column */}
                <div className="col-12 col-md-6 mb-4">
                  <Card className="card-db" style={{ padding: "20px", borderRadius: "8px" }}>
                    {error && (
                      <Alert
                        message={error}
                        type="error"
                        showIcon
                        style={{ marginBottom: "16px" }}
                      />
                    )}
                    {weatherLoading ? (
                      <div style={{ textAlign: "center", padding: "20px" }}>
                        <Spin size="large" />
                      </div>
                    ) : weatherData ? (
                      <div>
                        <h3>
                          Weather for {city} on {getDayOfWeek(weatherData.date)} (
                          {weatherData.date})
                        </h3>
                        <div
                          style={{
                            display: "flex",
                            gap: "20px",
                            marginTop: "16px",
                          }}
                        >
                          <div>
                            <p>
                              <strong>Min Temperature:</strong> {weatherData.minTemp}°C
                            </p>
                            <p>
                              <strong>Max Temperature:</strong> {weatherData.maxTemp}°C
                            </p>
                          </div>
                          <div>
                            <p>
                              <strong>Conditions:</strong> {weatherData.weatherDescription}
                            </p>
                          </div>
                        </div>
                        <Button
                          type="primary"
                          style={{ marginTop: "16px" }}
                          onClick={() => navigate("/weeklyforecast")}
                        >
                          View Weekly Forecast
                        </Button>
                      </div>
                    ) : (
                      <p>Enter a city name to see today's weather forecast</p>
                    )}
                  </Card>
                </div>
              </div>
            </div>
          </div>

          <div className="row">
            {/* Recommended Wardrobe Section */}
            <div className="col-12 col-md-6 mb-4">
              <h2>Recommended Wardrobe</h2>
              <Card className="card-db" style={{ padding: "20px", borderRadius: "8px" }}>
                {wardrobeLoading ? (
                  <div style={{ textAlign: "center" }}>
                    <Spin />
                  </div>
                ) : wardrobeData && wardrobeData.length > 0 ? (
                  <div>
                    <Tag
                      color="blue"
                      style={{ fontSize: "16px", padding: "8px", marginBottom: "12px" }}
                    >
                      {wardrobeData[0].theme}
                    </Tag>
                    <List className="card-db"
                      header={<strong>Recommended Items:</strong>}
                      bordered
                      dataSource={wardrobeData[0].clothingItems.map((item, index) => ({
                        item,
                        description: wardrobeData[0].clothingDescriptions[index],
                      }))}
                      renderItem={({ item, description }) => (
                        <List.Item>
                          <div className="text-white">
                            <span style={{ marginRight: "8px" }}>•</span>
                            <strong>{item}</strong>
                            <div className="text-white" style={{ color: "#666", marginTop: "4px" }}>
                              {description}
                            </div>
                          </div>
                        </List.Item>
                      )}
                    />
                    <Button
                      type="primary"
                      style={{ marginTop: "16px" }}
                      onClick={() => navigate("/recommendedwardrobe")}
                    >
                      View All Recommended Wardrobes
                    </Button>
                  </div>
                ) : (
                  <p>Enter a city name to get wardrobe recommendations</p>
                )}
              </Card>
            </div>

            {/* Recommended Activities Section */}
            <div className="col-12 col-md-6 mb-4">
              <h2>Recommended Activities</h2>
              <Card className="card-db" style={{ padding: "20px", borderRadius: "8px" }}>
                {activityLoading ? (
                  <div style={{ textAlign: "center" }}>
                    <Spin />
                  </div>
                ) : activityData && activityData.length > 0 ? (
                  <div>
                    <List className="text-white"
                      header={<strong>Recommended Activities:</strong>}
                      bordered
                      dataSource={activityData.slice(0, 3)}
                      renderItem={(activity) => (
                        <List.Item className="text-white">
                          <div>
                            <strong>{activity.name}</strong>
                            <div className="text-white" style={{ color: "#666", marginTop: "4px" }}>
                              {activity.description}
                            </div>
                          </div>
                        </List.Item>
                      )}
                    />
                    <Button
                      type="primary"
                      style={{ marginTop: "16px" }}
                      onClick={() => navigate("/recommendedactivity")}
                    >
                      View All Recommended Activities
                    </Button>
                  </div>
                ) : (
                  <p>No activity recommendations available for this city.</p>
                )}
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;