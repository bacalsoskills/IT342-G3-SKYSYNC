import React, { useState, useEffect } from "react";
import { List, Spin, Button } from "antd";
import { getUserNotifications, deleteNotificationById } from "../services/notificationService";
import { useNavigate } from "react-router-dom";
import UserHeader from "../components/userHeader";

const NotificationPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    if (userId) {
      fetchNotifications();
    }
  }, [userId]);

  const fetchNotifications = async () => {
    setLoading(true);
    try {
      const data = await getUserNotifications(userId);
      console.log("Notifications to Render (Sorted by ID):", data); // Debugging
      setNotifications(data);
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteNotification = async (notificationId) => {
    try {
      await deleteNotificationById(notificationId);
      setNotifications((prevNotifications) =>
        prevNotifications.filter((notification) => notification.id !== notificationId)
      );
    } catch (error) {
      console.error("Failed to delete notification:", error);
    }
  };

  return (
    <div>
      <UserHeader />
      <div
        className="container-fluid"
        style={{ minHeight: "100vh", background: "#fff", paddingTop: "24px" }}
      >
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8">
            <div className="d-flex justify-content-start mb-3">
              <Button type="default" onClick={() => navigate("/dashboard")}>
                Back to Dashboard
              </Button>
            </div>

        </div>
      </div>
    </div>
  );
};

export default NotificationPage;