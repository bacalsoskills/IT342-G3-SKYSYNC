import React, { useState, useEffect } from "react";
import { List, Spin, Button } from "antd";
import { getUserNotifications } from "../services/notificationService";
import { useNavigate } from "react-router-dom";

const NotificationPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  useEffect(() => {
    if (userId) {
      fetchNotifications();
    }
  }, [userId]);

  const fetchNotifications = async () => {
    setLoading(true);
    try {
      const data = await getUserNotifications(userId);
      setNotifications(data); // Notifications are already sorted in descending order
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/dashboard")} style={{ marginBottom: "20px" }}>
        Back to Dashboard
      </Button>
      <h2>All Notifications</h2>
      {loading ? (
        <Spin size="large" />
      ) : notifications.length > 0 ? (
        <List
          bordered
          dataSource={notifications}
          renderItem={(notification) => (
            <List.Item>
              <div>
                <strong>{notification.message}</strong>
              </div>
            </List.Item>
          )}
        />
      ) : (
        <p>No notifications available.</p>
      )}
    </div>
  );
};

export default NotificationPage;