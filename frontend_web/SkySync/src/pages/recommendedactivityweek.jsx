import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, Button, message } from "antd";
import { getActivityRecommendationsByCode, saveActivityForUser } from "../services/activityService";
import { useNavigate } from "react-router-dom";
import UserHeader from "../components/userHeader";

const RecommendedActivityWeek = () => {
  const [activityData, setActivityData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  // Retrieve the saved day data from localStorage
  const savedDayData = JSON.parse(localStorage.getItem("selectedDayData"));

  useEffect(() => {
    const fetchActivityData = async () => {
      if (!savedDayData) {
        message.error("No day data found. Redirecting to Weekly Forecast...");
        navigate("/weeklyforecast");
        return;
      }

      setLoading(true);
      setError(null);

      try {
        const data = await getActivityRecommendationsByCode(savedDayData.weatherCode);
        setActivityData(data); // Set the activity data
      } catch (err) {
        console.error("Error fetching activity recommendations:", err);
        setError("Failed to fetch activity recommendations. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchActivityData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // Empty dependency array to ensure it runs only once

  const handleAddActivity = async (activity) => {
    try {
      // Use the date from savedDayData instead of today's date
      const selectedDate = savedDayData.date; // Use the date from the weekly forecast
      localStorage.setItem("selectedScheduleDate", selectedDate);

      const savedActivity = await saveActivityForUser(userId, activity); // Save the activity and get the saved activity
      message.success("Activity added successfully!");
      // Navigate to the schedule activity page with the saved activity details
      navigate("/scheduleactivity", { state: { activity: savedActivity } });
    } catch (err) {
      console.error("Error adding activity:", err);
      message.error("Failed to add activity. Please try again.");
    }
  };

  return (
    <div className="min-vh-100 u-fixed-background">
      <UserHeader />
      <div className="container" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
        <Button type="default" onClick={() => navigate("/weeklyforecast")} style={{ marginBottom: "20px" }}>
          Back to Weekly Forecast
        </Button>
        <Button
          type="primary"
          onClick={() => {
            const selectedDate = savedDayData?.date || new Date().toISOString().split("T")[0]; // Use the selected date or default to today
            localStorage.setItem("selectedScheduleDate", selectedDate); // Save the selected date
            navigate("/useraddactivity");
          }}
          style={{ marginBottom: "20px", marginLeft: "10px" }}
        >
          Add Own Activity
        </Button>
        <h2>Recommended Activities for {savedDayData?.dayOfWeek}, {savedDayData?.date}</h2>
        <p>
          <strong>Weather:</strong> {savedDayData?.weatherDescription} <br />
          <strong>Min Temp:</strong> {savedDayData?.minTemp}°C <br />
          <strong>Max Temp:</strong> {savedDayData?.maxTemp}°C
        </p>
        <div className="row">
          {loading ? (
            <div className="col-12" style={{ textAlign: "center", padding: "20px" }}>
              <Spin size="large" />
            </div>
          ) : error ? (
            <div className="col-12">
              <Alert
                message={error}
                type="error"
                showIcon
                style={{ marginBottom: "16px" }}
              />
            </div>
          ) : activityData && activityData.length > 0 ? (
            activityData.map((activity, idx) => (
              <div className="col-12 col-md-6 col-lg-4 mb-4 d-flex" key={idx}>
                <Card
                  style={{
                    padding: "20px",
                    borderRadius: "8px",
                    height: "100%",
                    width: "100%",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "center", // Center content horizontally
                    textAlign: "center",  // Center text inside the card
                  }}
                >
                  <div>
                    <strong>{activity.name}</strong>
                    <div style={{ color: "#666", marginTop: "4px" }}>
                      {activity.description}
                    </div>
                  </div>
                  <Button
                    type="primary"
                    onClick={() => handleAddActivity(activity)}
                    style={{ marginTop: "16px" }}
                  >
                    Add Activity
                  </Button>
                </Card>
              </div>
            ))
          ) : (
            <div className="col-12">
              <p>No activity recommendations available for this weather condition.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RecommendedActivityWeek;