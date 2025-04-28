import React, { useState, useEffect } from "react";
import { Card, Spin, Alert, List, Button } from "antd";
import { useNavigate } from "react-router-dom";
import { getUserActivities } from "../services/activityService";
import UserHeader from "../components/userHeader";
import MyActivityList from "../components/MyActivityList";

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
    <div>
      <UserHeader />
      <div className="u-fixed-background container-fluid" style={{ minHeight: "92.5vh", paddingTop: "24px" }}>
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8">
            <div className="d-flex justify-content-start mb-3">
              <Button type="default" onClick={() => navigate("/dashboard")}>
                Back to Dashboard
              </Button>
            </div>
            <h2 className="mb-3">My Activities</h2>
            <MyActivityList activities={activities} loading={loading} error={error} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MyActivity;