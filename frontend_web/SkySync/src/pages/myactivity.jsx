import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Button } from "antd";
import { useNavigate } from "react-router-dom";
import { getUserActivities } from "../services/activityService";
import UserHeader from "../components/userHeader";

const MyActivity = () => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchUserActivities = async () => {
      setLoading(true);
      setError(null);

      try {
        const data = await getUserActivities(userId);

        // Sort activities in descending order by activityId
        const sortedActivities = data.sort((a, b) => b.activityId - a.activityId);

        setActivities(sortedActivities);
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
    <div>
      <UserHeader />
      <div className="container-fluid" style={{ minHeight: "100vh", background: "#fff", paddingTop: "24px" }}>
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8">
            <div className="d-flex justify-content-start mb-3">
              <Button type="default" onClick={() => navigate("/dashboard")}>
                Back to Dashboard
              </Button>
            </div>
            <h2 className="mb-3">My Activities</h2>
            <Card style={{ padding: "20px", borderRadius: "8px" }}>
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
                <p>No activities found.</p>
              )}
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyActivity;