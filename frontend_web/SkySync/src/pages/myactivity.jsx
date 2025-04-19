import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Button } from "antd";
import { useNavigate } from "react-router-dom";
import { getUserActivities } from "../services/activityService"; // Import the getUserActivities method

const MyActivity = () => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  useEffect(() => {
    const fetchUserActivities = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await getUserActivities(userId); // Use the getUserActivities method
        setActivities(data);
      } catch (err) {
        console.error("Error fetching user activities:", err);
        setError("Failed to fetch user activities. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchUserActivities();
  }, [userId]);

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>My Activities</h2>
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
        ) : activities && activities.length > 0 ? (
          <List
            header={<strong>Saved Activities:</strong>}
            bordered
            dataSource={activities}
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
                    onClick={() => navigate(`/activitydetails`, { state: { activity } })}
                  >
                    View Details
                  </Button>
                </div>
              </List.Item>
            )}
          />
        ) : (
          <p>No saved activities found.</p>
        )}
      </Card>
    </div>
  );
};

export default MyActivity;