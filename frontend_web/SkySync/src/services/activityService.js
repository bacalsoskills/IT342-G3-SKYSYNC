import axios from "axios";
import { API_BASE_URL } from "./config";

const API_URL = `${API_BASE_URL}/api/activities`;

export const getActivityRecommendationsByCity = async (city) => {
  try {
    const authToken = localStorage.getItem("authToken");
    const response = await axios.get(`${API_URL}/recommendationsByCity`, {
      params: { city },
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching activity recommendations:", error);
    throw error;
  }
};

export const saveActivityForUser = async (userId, activity) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.post(
      `${API_URL}`,
      activity,
      {
        params: { userId },
        headers: {
          Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
        },
      }
    );
    return response.data; // Return the saved activity with its activityId
  } catch (error) {
    console.error("Error saving activity for user:", error);
    throw error;
  }
};

// Fetch saved activities for a user
export const getUserActivities = async (userId) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.get(`${API_URL}/user`, {
      params: { userId },
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching user activities:", error);
    throw error;
  }
};

export const deleteActivity = async (activityId) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.delete(`${API_URL}`, {
      params: { activityId },
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error deleting activity:", error);
    throw error;
  }
};

export const getActivityRecommendationsByCode = async (weatherCode) => {
  try {
    const authToken = localStorage.getItem("authToken"); // Retrieve the token from localStorage
    const response = await axios.get(`${API_URL}/recommendationsByCode`, {
      params: { weatherCode },
      headers: {
        Authorization: `Bearer ${authToken}`, // Include the token in the Authorization header
      },
    });
    return response.data; // Return the list of recommended activities
  } catch (error) {
    console.error("Error fetching activity recommendations by code:", error);
    throw error;
  }
};