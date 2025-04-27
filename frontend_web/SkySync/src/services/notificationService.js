import axios from "axios";
import { API_BASE_URL } from "./config";

const API_URL = `${API_BASE_URL}/api/notifications`;

export const getUserNotifications = async (userId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.get(`${API_URL}/user/${userId}`, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return response.data.sort((a, b) => b.id - a.id);
  } catch (error) {
    console.error("Error fetching user notifications:", error);
    throw error;
  }
};
  
export const getUnreadNotifications = async (userId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.get(`${API_URL}/user/${userId}/unread`, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return response.data.sort((a, b) => b.id - a.id); // Sort notifications by ID (descending)
  } catch (error) {
    console.error("Error fetching unread notifications:", error);
    throw error;
  }
};

// Delete a specific notification by ID
export const deleteNotificationById = async (notificationId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    await axios.delete(`${API_URL}/${notificationId}`, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
  } catch (error) {
    console.error("Error deleting notification:", error);
    throw error;
  }
};

export const markNotificationAsRead = async (notificationId) => {
  try {
    const authToken = localStorage.getItem("authToken");
    await axios.patch(`${API_URL}/${notificationId}/mark-as-read`, null, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
  } catch (error) {
    console.error("Error marking notification as read:", error);
    throw error;
  }
};