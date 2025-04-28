import React, { useState, useEffect } from "react";
import { List, Spin, Button, message } from "antd";
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
      setNotifications(data);
    } catch (error) {
      console.error("Failed to fetch notifications:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteNotification = async (notificationId) => {
    try {
      await deleteNotificationById(notificationId); // Call the service to delete the notification
      message.success("Notification deleted successfully.");
      setNotifications((prevNotifications) =>
        prevNotifications.filter((notification) => notification.id !== notificationId)
      ); // Remove the deleted notification from the state
    } catch (error) {
      console.error("Failed to delete notification:", error);
      message.error("Failed to delete notification.");
    }
  };

  return (
    <div>
      <UserHeader />
      <div
        className="u-fixed-background container-fluid"
        style={{ minHeight: "92.5vh", paddingTop: "24px" }}
      >
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8">
            <div className="d-flex justify-content-start mb-3">
              <Button type="default" onClick={() => navigate("/dashboard")}>
                Back to Dashboard
              </Button>
            </div>
            <h2 className="mb-3">All Notifications</h2>
            {loading ? (
              <div className="text-center py-5">
                <Spin size="large" />
              </div>
            ) : notifications.length > 0 ? (
              <List
                className="bg-white"
                bordered
                dataSource={notifications}
                renderItem={(notification) => (
                  <List.Item
                    actions={[
                      <Button
                        key="delete"
                        type="text"
                        danger
                        onClick={() => handleDeleteNotification(notification.id)}
                      >
                        X
                      </Button>,
                    ]}
                  >
                    <div>
                      <strong
                        style={{
                          textDecoration: notification.isRead ? "line-through" : "none",
                        }}
                      >
                        {notification.message}
                      </strong>
                    </div>
                  </List.Item>
                )}
              />
            ) : (
              <p>No notifications available.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotificationPage;