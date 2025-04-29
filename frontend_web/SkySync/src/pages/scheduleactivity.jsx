import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Form, TimePicker, Button, message, Row, Col, Input } from "antd"; // Add Input here
import { scheduleActivity } from "../services/scheduleService";
import moment from "moment"; // Ensure moment is installed
import UserHeader from "../components/userHeader";

const ScheduleActivity = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { activity } = location.state || {}; // Get the activity passed from the previous page
  const userId = localStorage.getItem("userId"); // Retrieve the user ID from localStorage

  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const { startTime, endTime } = values;

      // Automatically set today's date and combine it with the selected times
      const today = moment().format("YYYY-MM-DD");
      const formattedStartTime = `${today}T${startTime.format("HH:mm")}`;
      const formattedEndTime = `${today}T${endTime.format("HH:mm")}`;

      console.log("Formatted Start Time:", formattedStartTime);
      console.log("Formatted End Time:", formattedEndTime);

      await scheduleActivity(userId, activity.activityId, formattedStartTime, formattedEndTime); // Send formatted times to the backend

      // Show success message
      message.success("Activity scheduled successfully! Redirecting to dashboard...");

      // Redirect to the dashboard after a short delay
      setTimeout(() => {
        navigate("/dashboard");
      }, 2000); // 2-second delay
    } catch (err) {
      console.error("Error scheduling activity:", err);
      message.error("Failed to schedule activity. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-vh-100 u-fixed-background" >
      <UserHeader />
    <div className="col-12 col-md-10 col-lg-8 mx-auto" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <Button type="default" onClick={() => navigate("/recommendedactivity")} style={{ marginBottom: "20px" }}>
        Back to Recommended Activities
      </Button>
      <h2>Schedule Activity</h2>
      {activity ? (
        <Form
          layout="vertical"
          onFinish={onFinish}
          initialValues={{
            startTime: null,
            endTime: null,
          }}
        >
          <Form.Item label="Activity Name">
            <Input value={activity.name} disabled />
          </Form.Item>
          <Row gutter={8}>
            <Col span={12}>
              <Form.Item
                label="Start Time"
                name="startTime"
                rules={[{ required: true, message: "Please select a start time!" }]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="End Time"
                name="endTime"
                rules={[{ required: true, message: "Please select an end time!" }]}
              >
                <TimePicker
                  format="HH:mm"
                  hideDisabledOptions // Hides disabled options for cleaner UI
                  renderExtraFooter={() => null} // Ensures the footer is completely removed
                />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Schedule Activity
            </Button>
          </Form.Item>
        </Form>
      ) : (
        <p>No activity selected. Please go back and select an activity.</p>
      )}
    </div>
    </div>
  );
};

export default ScheduleActivity;