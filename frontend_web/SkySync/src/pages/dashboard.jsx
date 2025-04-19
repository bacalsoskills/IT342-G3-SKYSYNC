import React, { useState, useEffect } from "react";
import { Menu, Dropdown, Button, Card, Input, Spin, Alert, List, Tag } from "antd";
import { UserOutlined, BellOutlined, LogoutOutlined } from "@ant-design/icons";
import { getUserDetails } from "../services/userService";
import { logout } from "../services/authService";
import { getTodaysWeatherByCity } from "../services/weatherService";
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";
import { getActivityRecommendationsByCity } from "../services/activityService";
import { getUserNotifications } from "../services/notificationService"; // Import the notification service
import dayjs from "dayjs";
import weekday from "dayjs/plugin/weekday";
import { useNavigate } from "react-router-dom"; // Import useNavigate

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
  const [city, setCity] = useState(() => {
    // Retrieve the last saved city from localStorage or default to "Cebu"
    return localStorage.getItem("lastCity") || "Cebu";
  });
  const [weatherLoading, setWeatherLoading] = useState(false);
  const [wardrobeLoading, setWardrobeLoading] = useState(false);
  const [activityLoading, setActivityLoading] = useState(false); // Loading state for activities
  const [error, setError] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [loadingNotifications, setLoadingNotifications] = useState(false);
  const navigate = useNavigate(); // Initialize navigate

  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  useEffect(() => {
    const authToken = localStorage.getItem("authToken");

    if (!authToken || !userId) {
      alert("You are not logged in. Redirecting to login...");
      window.location.href = "/login";
    } else {
      fetchUserDetails(userId);
    }

    // Fetch initial data for default city
    fetchWeatherData(city);
    fetchActivityData(city); // Fetch activities for the default city

    if (userId) {
      fetchNotifications();
    }
  }, [userId]);

  useEffect(() => {
    if (userId) {
      fetchNotifications();
    }
  }, [userId]);

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

  const fetchNotifications = async () => {
    setLoadingNotifications(true);
    try {
      const data = await getUserNotifications(userId);
      setNotifications(data.slice(0, 5)); // Limit to the 5 most recent notifications
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
    } finally {
      setLoadingNotifications(false);
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

  const handleCityChange = (newCity) => {
    setCity(newCity);
    localStorage.setItem("lastCity", newCity); // Save the city to localStorage
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

  const notificationMenu = (
    <Menu>
      {loadingNotifications ? (
        <Menu.Item key="loading">
          <Spin size="small" /> Loading notifications...
        </Menu.Item>
      ) : (
        <>
          {notifications.length > 0 ? (
            notifications.map((notification) => (
              <Menu.Item key={notification.id}>
                <div style={{ whiteSpace: "normal" }}>{notification.message}</div>
              </Menu.Item>
            ))
          ) : (
            <Menu.Item key="no-notifications">No notifications available</Menu.Item>
          )}
          <Menu.Divider />
          <Menu.Item key="view-all">
            <Button type="link" onClick={() => navigate("/notifications")}>
              View All Notifications
            </Button>
          </Menu.Item>
        </>
      )}
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

        {/* User Profile and Notifications */}
        <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
          <Dropdown overlay={userMenu} trigger={["click"]}>
            <Button type="text" style={{ display: "flex", alignItems: "center" }}>
              <UserOutlined style={{ marginRight: "8px" }} />
              {loading ? "Loading..." : `${user.firstName} ${user.lastName}`}
            </Button>
          </Dropdown>
          <Button type="link" onClick={() => navigate("/myactivity")}>
            My Activities
          </Button>
          <Dropdown overlay={notificationMenu} trigger={["click"]}>
            <BellOutlined style={{ fontSize: "20px", cursor: "pointer" }} />
          </Dropdown>
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
              onChange={(e) => handleCityChange(e.target.value)} // Update city and save to localStorage
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
              {/* Add Weekly Forecast Button */}
              <Button
                type="primary"
                style={{ marginTop: "16px" }}
                onClick={() => navigate("/weeklyforecast")} // Navigate to WeeklyForecast
              >
                View Weekly Forecast
              </Button>
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
              {/* Add View All Recommended Wardrobes Button */}
              <Button
                type="primary"
                style={{ marginTop: "16px" }}
                onClick={() => navigate("/recommendedwardrobe")} // Navigate to RecommendedWardrobe page
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
            <div>
              <List
                header={<strong>Recommended Activities:</strong>}
                bordered
                dataSource={activityData.slice(0, 3)} // Display only the first 3 activities
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
              {/* Add View All Recommended Activities Button */}
              <Button
                type="primary"
                style={{ marginTop: "16px" }}
                onClick={() => navigate("/recommendedactivity")} // Navigate to RecommendedActivity page
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
  );
};

export default Dashboard;