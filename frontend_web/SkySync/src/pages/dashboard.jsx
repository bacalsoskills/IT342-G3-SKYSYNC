import React, { useState, useEffect } from "react";
import { Menu, Dropdown, Button, Card, Input, Spin, Alert, List, Tag } from "antd";
import { UserOutlined, BellOutlined, LogoutOutlined } from "@ant-design/icons";
import { getUserDetails } from "../services/userService";
import { logout } from "../services/authService";
import { getTodaysWeatherByCity } from "../services/weatherService";
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";
import { getActivityRecommendationsByCity } from "../services/activityService"; // Import the new service
import dayjs from "dayjs";
import weekday from "dayjs/plugin/weekday";

dayjs.extend(weekday);

const Dashboard = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
  });
  const [loading, setLoading] = useState(true);
  const [weatherData, setWeatherData] = useState(null);
  const [wardrobeData, setWardrobeData] = useState(null);
  const [activityData, setActivityData] = useState([]); // State for activity recommendations
  const [city, setCity] = useState("Cebu");
  const [weatherLoading, setWeatherLoading] = useState(false);
  const [wardrobeLoading, setWardrobeLoading] = useState(false);
  const [activityLoading, setActivityLoading] = useState(false); // Loading state for activities
  const [error, setError] = useState(null);

  useEffect(() => {
    const authToken = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    if (!authToken || !userId) {
      alert("You are not logged in. Redirecting to login...");
      window.location.href = "/login";
    } else {
      fetchUserDetails(userId);
    }

    // Fetch initial data for default city
    fetchWeatherData("Cebu");
    fetchActivityData("Cebu"); // Fetch activities for the default city
  }, []);

  const fetchUserDetails = async (userId) => {
    try {
      const authToken = localStorage.getItem("authToken"); // Always get the latest token
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
      setWeatherData(data);
      // After getting weather, fetch wardrobe recommendations
      await fetchWardrobeData(cityName);
    } catch (err) {
      console.error("Error fetching weather data:", err);
      setError("Failed to fetch weather data. Please try again.");
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
      setActivityData(data); // Set the activity recommendations
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
    window.location.href = "/userprofile";
  };

  const userMenu = (
    <Menu>
      <Menu.Item key="profile" onClick={handleProfile}>
        <UserOutlined /> User Profile
      </Menu.Item>
      <Menu.Item key="logout" onClick={handleLogout}>
        <LogoutOutlined /> Logout
      </Menu.Item>
    </Menu>
  );

  const getDayOfWeek = (dateString) => {
    return dayjs(dateString).format("dddd");
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      {/* Top Bar */}
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "20px",
        }}
      >
        <div></div>

        {/* User Profile and Notification */}
        <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
          <Dropdown overlay={userMenu} trigger={["click"]}>
            <Button type="text" style={{ display: "flex", alignItems: "center" }}>
              <UserOutlined style={{ marginRight: "8px" }} />
              {loading
                ? "Loading..."
                : `${user.firstName} ${user.lastName}`}
            </Button>
          </Dropdown>
          <BellOutlined style={{ fontSize: "20px", cursor: "pointer" }} />
        </div>
      </div>

      {/* Weather Forecast Section */}
      <div style={{ marginBottom: "20px" }}>
        <h2>Weather Forecast</h2>
        <Card
          style={{
            padding: "20px",
            borderRadius: "8px",
          }}
        >
          <div style={{ marginBottom: "16px" }}>
            <Input.Search
              placeholder="Enter city name"
              enterButton="Search"
              size="large"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              onSearch={() => {
                fetchWeatherData(city);
                fetchActivityData(city); // Fetch activities when searching for a city
              }}
              loading={weatherLoading}
            />
          </div>

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
              <div style={{ display: "flex", gap: "20px", marginTop: "16px" }}>
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
            </div>
          ) : (
            <p>Enter a city name to see today's weather forecast</p>
          )}
        </Card>
      </div>

      {/* Recommended Wardrobe Section */}
      <div style={{ marginBottom: "20px" }}>
        <h2>Recommended Wardrobe</h2>
        <Card
          style={{
            padding: "20px",
            borderRadius: "8px",
          }}
        >
          {wardrobeLoading ? (
            <div style={{ textAlign: "center" }}>
              <Spin />
            </div>
          ) : wardrobeData && wardrobeData.length > 0 ? (
            <div>
              <Tag color="blue" style={{ fontSize: "16px", padding: "8px", marginBottom: "12px" }}>
                {wardrobeData[0].theme}
              </Tag>
              <List
                header={<strong>Recommended Items:</strong>}
                bordered
                dataSource={wardrobeData[0].clothingItems.map((item, index) => ({
                  item,
                  description: wardrobeData[0].clothingDescriptions[index],
                }))}
                renderItem={({ item, description }) => (
                  <List.Item>
                    <div>
                      <span style={{ marginRight: "8px" }}>•</span>
                      <strong>{item}</strong>
                      <div style={{ color: "#666", marginTop: "4px" }}>
                        {description}
                      </div>
                    </div>
                  </List.Item>
                )}
              />
            </div>
          ) : (
            <p>Enter a city name to get wardrobe recommendations</p>
          )}
        </Card>
      </div>

      {/* Recommended Activities Section */}
      <div>
        <h2>Recommended Activities</h2>
        <Card
          style={{
            padding: "20px",
            borderRadius: "8px",
          }}
        >
          {activityLoading ? (
            <div style={{ textAlign: "center" }}>
              <Spin />
            </div>
          ) : activityData && activityData.length > 0 ? (
            <List
              header={<strong>Recommended Activities:</strong>}
              bordered
              dataSource={activityData}
              renderItem={(activity) => (
                <List.Item>
                  <div>
                    <strong>{activity.name}</strong>
                    <div style={{ color: "#666", marginTop: "4px" }}>
                      {activity.description}
                    </div>
                  </div>
                </List.Item>
              )}
            />
          ) : (
            <p>No activity recommendations available for this city.</p>
          )}
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;