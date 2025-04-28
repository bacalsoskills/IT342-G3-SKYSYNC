import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Tag, Button } from "antd";
import { Carousel } from "antd";
import { useNavigate } from "react-router-dom";
import { getWardrobeRecommendationByCity } from "../services/wardrobeService";
import themeImages from "../services/themeImages"; // Correct path to the services folder
import "../styles/Carousel.css"; // Import the Carousel CSS file
import UserHeader from "../components/userHeader"; // Import UserHeader

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
    <div className="min-vh-100 u-fixed-background pb-5">
      <UserHeader />
      <div className="container mt-4 ">
        <Button
          type="default"
          onClick={() => navigate("/dashboard")}
          style={{ marginBottom: "20px" }}
        >
          Back to Dashboard
        </Button>
        <h2 className="mb-4">All Recommended Wardrobes for {city}</h2>
        <Card className="bg-transparent" style={{ padding: "20px", borderRadius: "8px" }}>
          {loading ? (
            <div className="text-center py-4">
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
            <div className="d-flex flex-column gap-4">
              {wardrobeData.map((recommendation, index) => (
                <Card key={index} className="cs-shadow w-100 p-3">
                  <div className="d-flex flex-column flex-md-row align-items-center">
                    {/* Left Column: Wardrobe Details */}
                    <div className="flex-grow-1">
                      <Tag
                        color="blue"
                        style={{
                          fontSize: "16px",
                          padding: "8px",
                          marginBottom: "12px",
                        }}
                      >
                        {recommendation.theme}
                      </Tag>
                      <List
                        header={<strong>Recommended Items:</strong>}
                        bordered
                        dataSource={recommendation.clothingItems.map(
                          (item, idx) => ({
                            item,
                            description:
                              recommendation.clothingDescriptions[idx],
                          })
                        )}
                        renderItem={({ item, description }) => (
                          <List.Item>
                            <div>
                              <span className="me-2">•</span>
                              <strong>{item}</strong>
                              <div
                                style={{ color: "#666", marginTop: "4px" }}
                              >
                                {description}
                              </div>
                            </div>
                          </List.Item>
                        )}
                      />
                    </div>

                    {/* Right Column: Carousel */}
                    <div
                      className="mt-3 mt-md-0 ms-md-4"
                      style={{
                        width: "300px",
                        height: "300px",
                        overflow: "hidden",
                        borderRadius: "8px",
                      }}
                    >
                      <Carousel autoplay autoplaySpeed={2000} arrows>
                        {getImagesForTheme(recommendation.theme).map(
                          (image, idx) => (
                            <div key={idx}>
                              <img
                                src={image}
                                alt={`${recommendation.theme} ${idx + 1}`}
                                style={{
                                  width: "100%",
                                  height: "100%",
                                  objectFit: "cover",
                                  borderRadius: "8px",
                                }}
                              />
                            </div>
                          )
                        )}
                      </Carousel>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          ) : (
            <p>No wardrobe recommendations available for this city.</p>
          )}
        </Card>
      </div>
    </div>
  );
};

export default RecommendedWardrobe;
