import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Button, message } from "antd";
import { getActivityRecommendationsByCity, saveActivityForUser } from "../services/activityService";
import { useNavigate } from "react-router-dom";

const RecommendedActivity = () => {
  const [city, setCity] = useState(() => {
    return localStorage.getItem("lastCity") || "Cebu";
  });
  const [activityData, setActivityData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchActivityData = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await getActivityRecommendationsByCity(city);
        setActivityData(data);
      } catch (err) {
        console.error("Error fetching activity recommendations:", err);
        setError("Failed to fetch activity recommendations. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchActivityData();
  }, [city]);

  const handleAddActivity = async (activity) => {
    try {
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
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>All Recommended Activities for {city}</h2>
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
        ) : activityData && activityData.length > 0 ? (
          <List
            header={<strong>Recommended Activities:</strong>}
            bordered
            dataSource={activityData}
            renderItem={(activity) => (
              <List.Item>
                <div style={{ display: "flex", justifyContent: "space-between", width: "100%" }}>
                  <div>
                    <strong>{activity.name}</strong>
                    <div style={{ color: "#666", marginTop: "4px" }}>
                      {activity.description}
                    </div>
                  </div>
                  <Button
                    type="primary"
                    onClick={() => handleAddActivity(activity)}
                  >
                    Add Activity
                  </Button>
                </div>
              </List.Item>
            )}
          />
        ) : (
          <p>No activity recommendations available for this city.</p>
        )}
      </Card>
    </div>
  );
};

export default RecommendedActivity;