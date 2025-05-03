import React, { useState, useEffect } from "react";
import { Form, Input, Button, message } from "antd";
import { useNavigate } from "react-router-dom";
import { saveActivityForUser } from "../services/activityService";

const UserAddActivity = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  // Dynamically fetch the saved date from localStorage
  const [savedDate, setSavedDate] = useState("");

  useEffect(() => {
    // Fetch the selected date from localStorage
    const dateFromStorage = localStorage.getItem("selectedScheduleDate") || new Date().toISOString().split("T")[0];
    setSavedDate(dateFromStorage); // Set the saved date dynamically
  }, []);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // Add the default weather condition to the form values
      const activityData = {
        ...values,
        weatherCondition: "Weather Condition", // Default value
      };

      // Call the saveActivityForUser service to save the activity
      const savedActivity = await saveActivityForUser(userId, activityData);

      // Save the selected date (from localStorage or today's date) for scheduling
      localStorage.setItem("selectedScheduleDate", savedDate);

      message.success("Activity added successfully!");

      // Navigate to the schedule activity page with the saved activity details
      navigate("/scheduleactivity", { state: { activity: savedActivity } });
    } catch (err) {
      console.error("Error adding activity:", err);
      message.error("Failed to add activity. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 u-fixed-background">
      <div className="container" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
        <Button type="default" onClick={() => navigate(-1)} style={{ marginBottom: "20px" }}>
          Back
        </Button>
        <h2>Add Your Own Activity</h2>
        <Form
          layout="vertical"
          onFinish={onFinish}
          style={{ maxWidth: "600px", margin: "0 auto" }}
        >
          <Form.Item
            label="Activity Name"
            name="name"
            rules={[{ required: true, message: "Please enter the activity name!" }]}
          >
            <Input placeholder="Enter activity name" />
          </Form.Item>
          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: "Please enter a description!" }]}
          >
            <Input.TextArea placeholder="Enter activity description" rows={4} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Add Activity
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default UserAddActivity;