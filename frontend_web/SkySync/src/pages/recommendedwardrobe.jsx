import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Tag, Button, message } from "antd"; // Import Button and message
import { useNavigate } from "react-router-dom"; // Import useNavigate
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";

const RecommendedWardrobe = () => {
  const [city, setCity] = useState(() => {
    return localStorage.getItem("lastCity") || "Cebu";
  });
  const [wardrobeData, setWardrobeData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate(); // Initialize navigate

  useEffect(() => {
    const fetchWardrobeData = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await getWardrobeRecommendationByCity(city);
        setWardrobeData(data);
      } catch (err) {
        console.error("Error fetching wardrobe recommendations:", err);
        setError("Failed to fetch wardrobe recommendations. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchWardrobeData();
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
      <h2>All Recommended Wardrobes for {city}</h2>
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
        ) : wardrobeData && wardrobeData.length > 0 ? (
          wardrobeData.map((recommendation, index) => (
            <div key={index} style={{ marginBottom: "20px" }}>
              <Tag color="blue" style={{ fontSize: "16px", padding: "8px", marginBottom: "12px" }}>
                {recommendation.theme}
              </Tag>
              <List
                header={<strong>Recommended Items:</strong>}
                bordered
                dataSource={recommendation.clothingItems.map((item, idx) => ({
                  item,
                  description: recommendation.clothingDescriptions[idx],
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
          ))
        ) : (
          <p>No wardrobe recommendations available for this city.</p>
        )}
      </Card>
    </div>
  );
};

export default RecommendedWardrobe;