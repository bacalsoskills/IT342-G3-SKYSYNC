import React, { useState, useEffect } from "react";
import { Button, Input, Form, message, Modal } from "antd"; // Add Modal to imports
import { getUserDetails, updateUserDetails, deleteUser } from "../services/userService";
import { useNavigate } from "react-router-dom";

const UserProfile = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false); // State for delete modal
  const [form] = Form.useForm();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserProfile = async () => {
      const authToken = localStorage.getItem("authToken");
      const userId = localStorage.getItem("userId");

      if (!authToken || !userId) {
        message.error("You are not logged in. Redirecting to login...");
        window.location.href = "/login";
        return;
      }

      try {
        const userDetails = await getUserDetails(userId, authToken);
        setUser(userDetails);
        form.setFieldsValue(userDetails); // Populate form with user details
      } catch (error) {
        console.error("Failed to fetch user details:", error);
        message.error("Failed to load user profile.");
      }
    };

    fetchUserProfile();
  }, [form]);

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  };

  const handleSaveChanges = async (values) => {
    const authToken = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    if (!authToken || !userId) {
      message.error("You are not logged in. Redirecting to login...");
      window.location.href = "/login";
      return;
    }

    if (values.newPassword && values.newPassword !== values.confirmPassword) {
      message.error("Passwords do not match.");
      return;
    }

    try {
      const updatedUser = {
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
        ...(values.newPassword && { password: values.newPassword }),
      };

      console.log("Sending updated user details:", updatedUser);

      await updateUserDetails(userId, authToken, updatedUser);

      localStorage.setItem("userFirstName", values.firstName);
      localStorage.setItem("userLastName", values.lastName);

      message.success("Changes saved successfully.");

      setUser({
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
      });
      form.setFieldsValue({
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
      });
      setIsEditing(false);
    } catch (error) {
      console.error("Failed to update user details:", error);
      message.error("Failed to update profile.");
    }
  };

  const handleBackToDashboard = () => {
    navigate("/dashboard"); // Navigate back to the dashboard
  };

  const handleDeleteAccount = async () => {
    const authToken = localStorage.getItem("authToken");
    const userId = localStorage.getItem("userId");

    if (!authToken || !userId) {
      message.error("You are not logged in.");
      return;
    }

    try {
      await deleteUser(userId, authToken);
      message.success("Account deleted successfully.");

      // Clear user data from local storage
      localStorage.removeItem("authToken");
      localStorage.removeItem("userId");
      localStorage.removeItem("userFirstName");
      localStorage.removeItem("userLastName");

      // Navigate to home page
      navigate("/");
    } catch (error) {
      console.error("Failed to delete account:", error);
      message.error("Failed to delete account.");
    }
  };

  const showDeleteConfirmation = () => {
    setIsDeleteModalVisible(true);
  };

  const handleCancelDelete = () => {
    setIsDeleteModalVisible(false);
  };

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <h1>User Profile</h1>
      <Form
        form={form}
        layout="vertical"
        onFinish={handleSaveChanges}
        initialValues={user}
      >
        <Form.Item
          label="First Name"
          name="firstName"
          rules={[{ required: true, message: "First name is required" }]}
        >
          <Input disabled={!isEditing} />
        </Form.Item>
        <Form.Item
          label="Last Name"
          name="lastName"
          rules={[{ required: true, message: "Last name is required" }]}
        >
          <Input disabled={!isEditing} />
        </Form.Item>
        <Form.Item
          label="Email"
          name="email"
          rules={[
            { required: true, message: "Email is required" },
            { type: "email", message: "Please enter a valid email" },
          ]}
        >
          <Input disabled={!isEditing} />
        </Form.Item>
        {isEditing && (
          <>
            <Form.Item
              label="New Password"
              name="newPassword"
              rules={[
                { min: 6, message: "Password must be at least 6 characters" },
              ]}
            >
              <Input.Password />
            </Form.Item>
            <Form.Item
              label="Confirm Password"
              name="confirmPassword"
              rules={[
                { min: 6, message: "Password must be at least 6 characters" },
              ]}
            >
              <Input.Password />
            </Form.Item>
          </>
        )}
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <Button type="primary" onClick={toggleEdit}>
            {isEditing ? "Cancel" : "Edit"}
          </Button>
          {isEditing && (
            <Button type="primary" htmlType="submit">
              Save Changes
            </Button>
          )}
        </div>
      </Form>
      <div style={{ marginTop: "20px", display: "flex", justifyContent: "space-between" }}>
        <Button type="default" onClick={handleBackToDashboard}>
          Back to Dashboard
        </Button>
        <Button
          type="danger"
          onClick={showDeleteConfirmation}
          style={{ marginLeft: "10px" }}
        >
          Delete Account
        </Button>
      </div>

      {/* Delete Confirmation Modal */}
      <Modal
        title="Confirm Account Deletion"
        visible={isDeleteModalVisible}
        onOk={handleDeleteAccount}
        onCancel={handleCancelDelete}
        okText="Yes, Delete"
        cancelText="No, Keep Account"
        okButtonProps={{ danger: true }}
      >
        <p>Are you sure you want to delete your account? This action cannot be undone.</p>
      </Modal>
    </div>
  );
};

export default UserProfile;