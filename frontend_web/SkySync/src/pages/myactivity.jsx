import React, { useState, useEffect } from "react";
import { Button, Spin, Alert } from "antd";
import { useNavigate } from "react-router-dom";
import { getUserActivities } from "../services/activityService";
import UserHeader from "../components/userHeader";
import MyActivityList from "../components/MyActivityList";
import ActivityDetailsModal from "../components/ActivityDetailsModal";

const MyActivity = () => {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedActivity, setSelectedActivity] = useState(null); // Track selected activity
  const [isModalVisible, setIsModalVisible] = useState(false); // Track modal visibility
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

  const handleViewDetails = (activity) => {
    setSelectedActivity(activity);
    setIsModalVisible(true); // Open the modal
  };

  const handleCloseModal = () => {
    setIsModalVisible(false); // Close the modal
    setSelectedActivity(null);
  };

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
            {loading ? (
              <Spin size="large" />
            ) : error ? (
              <Alert message={error} type="error" showIcon />
            ) : (
              <div className="mb-4">
                <MyActivityList
                  activities={activities}
                  onViewDetails={handleViewDetails}
                />
              </div>
            )}
          </div>
        </div>
      </div>
      {selectedActivity && (
        <ActivityDetailsModal
          visible={isModalVisible}
          onClose={handleCloseModal}
          activity={selectedActivity}
        />
      )}
    </div>
  );
};

export default MyActivity;