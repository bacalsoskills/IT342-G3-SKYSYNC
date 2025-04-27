import axios from "axios";
import { API_BASE_URL } from "./config";

const API_URL = `${API_BASE_URL}/api/users`;

export const registerUser = async (user) => {
  try {
    const response = await axios.post(API_URL, user);
    return response.data;
  } catch (error) {
    console.error("Registration error:", error.response?.data || error.message);
    throw new Error(error.response?.data?.message || "Registration failed");
  }
};

// Fetch user details using the userId
export const getUserDetails = async (userId, token) => {
  try {
    const response = await axios.get(`${API_URL}?id=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Return user details
  } catch (error) {
    console.error("Error fetching user details:", error);
    throw error;
  }
};

export const updateUserDetails = async (userId, token, userDetails) => {
  try {
    const response = await axios.put(`${API_URL}?id=${userId}`, userDetails, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // Return the updated user details and token
  } catch (error) {
    console.error("Error updating user details:", error);
    throw error;
  }
};

export const deleteUser = async (userId, token) => {
  try {
    const response = await axios.delete(`${API_URL}?id=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error deleting user:", error);
    throw error;
  }
};