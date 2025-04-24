import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Tag, Button } from "antd";
import { Carousel } from "antd";
import { useNavigate } from "react-router-dom";
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";
import themeImages from "../services/themeImages"; // Correct path to the services folder
import "../styles/Carousel.css"; // Import the Carousel CSS file

const RecommendedWardrobe = () => {
  const [city, setCity] = useState(() => localStorage.getItem("lastCity") || "Cebu");
  const [wardrobeData, setWardrobeData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

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

  const getImagesForTheme = (theme) => {
    return themeImages[theme] || []; // Retrieve images for the theme or return an empty array
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>All Recommended Wardrobes for {city}</h2>
      <Card style={{ padding: "20px", borderRadius: "8px" }}>
        {loading ? (
          <div style={{ textAlign: "center", padding: "20px" }}>
            <Spin size="large" />
          </div>
        ) : error ? (
          <Alert message={error} type="error" showIcon style={{ marginBottom: "16px" }} />
        ) : wardrobeData && wardrobeData.length > 0 ? (
          wardrobeData.map((recommendation, index) => (
            <div key={index} style={{ marginBottom: "20px", display: "flex", alignItems: "center" }}>
              <div style={{ flex: 1 }}>
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
                        <div style={{ color: "#666", marginTop: "4px" }}>{description}</div>
                      </div>
                    </List.Item>
                  )}
                />
              </div>
              <div style={{ flex: 1, marginLeft: "20px", display: "flex", justifyContent: "center", alignItems: "center" }}>
                <div style={{ width: "300px", height: "350px", overflow: "hidden" }}>
                  <Carousel
                    autoplay
                    autoplaySpeed={2000}
                    arrows
                  >
                    {getImagesForTheme(recommendation.theme).map((image, idx) => (
                      <div key={idx}>
                        <img
                          src={image}
                          alt={`${recommendation.theme} ${idx + 1}`}
                          style={{
                            width: "100%", // Make the image fill the container width
                            height: "100%", // Make the image fill the container height
                            objectFit: "cover", // Ensure the image fits within the dimensions
                            borderRadius: "8px", // Optional: Add rounded corners
                          }}
                        />
                      </div>
                    ))}
                  </Carousel>
                </div>
              </div>
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
